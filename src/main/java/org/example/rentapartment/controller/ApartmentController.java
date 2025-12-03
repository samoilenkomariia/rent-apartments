package org.example.rentapartment.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.github.fge.jsonpatch.Patch;
import jakarta.servlet.ServletRequest;
import org.example.rentapartment.model.apartment.Apartment;
import org.example.rentapartment.model.apartment.ApartmentDTO;
import org.example.rentapartment.model.apartment.ApartmentSearchDTO;
import org.example.rentapartment.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;

@RequestMapping("apartments")
@RestController
public class ApartmentController implements ApartmentControllerApi {
    private final ApartmentService apartmentService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ApartmentController(ApartmentService apartmentService,  ObjectMapper objectMapper) {
        this.apartmentService = apartmentService;
        this.objectMapper = objectMapper;
    }
    private ApartmentDTO mapToDTO(Apartment apartment) {
        return new ApartmentDTO(
                apartment.getId(),
                apartment.getPrice(),
                apartment.getAddress(),
                apartment.getParameters(),
                apartment.getDescription(),
                apartment.getLandlord().getId()
        );
    }

    @GetMapping
    @Override
    public ResponseEntity<Collection<ApartmentDTO>> getAll() {
        Collection<ApartmentDTO> dtos = apartmentService.findAll().stream()
                .map(this::mapToDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ApartmentDTO> getById(@PathVariable Long id) {
        return apartmentService.findById(id)
                .map(this::mapToDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Override
    public ResponseEntity<ApartmentDTO> create(@RequestBody ApartmentDTO dto) {
        Apartment newApart;
        try {
            newApart = apartmentService.create(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").buildAndExpand(newApart.getId()).toUri();
        return ResponseEntity.created(uri).body(mapToDTO(newApart));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<ApartmentDTO> update(@PathVariable Long id, @RequestBody ApartmentDTO aptDto) {
        if (aptDto.getId() != null && !aptDto.getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Apartment updated = apartmentService.update(id, aptDto);
            return  ResponseEntity.ok(mapToDTO(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/search/byRegion")
    @Override
    public ResponseEntity<Collection<ApartmentDTO>> searchByRegion(@ModelAttribute ApartmentSearchDTO searchDTO) {
        Collection<ApartmentDTO> apartments = apartmentService.findByRegion(searchDTO)
                .stream()
                .map(this::mapToDTO)
                .toList();
        return ResponseEntity.ok(apartments);
    }

    @GetMapping("/search/byCity")
    @Override
    public ResponseEntity<Collection<ApartmentDTO>> searchByCity(@ModelAttribute ApartmentSearchDTO searchDTO) {
        Collection<ApartmentDTO> apartments = apartmentService.findByCity(searchDTO)
                .stream()
                .map(this::mapToDTO)
                .toList();
        return ResponseEntity.ok(apartments);
    }

    @GetMapping("/search/byPrice")
    @Override
    public ResponseEntity<Collection<ApartmentDTO>> searchByPrice(@RequestParam BigDecimal price) {
        Collection<ApartmentDTO> dtos = apartmentService.findByPriceLessThan(price)
                .stream()
                .map(this::mapToDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    @Override
    public ResponseEntity<ApartmentDTO> jsonPatch(@PathVariable Long id, @RequestBody JsonPatch patch) {
        return patch(id, patch);
    }

    @PatchMapping(path = "/{id}", consumes = "application/merge-patch+json")
    @Override
    public ResponseEntity<ApartmentDTO> jsonMergePatch(@PathVariable Long id, @RequestBody JsonMergePatch patch) {
        return patch(id, patch);
    }

    private ResponseEntity<ApartmentDTO> patch(Long id, Patch patch) {
        Optional<Apartment> aptOptional = apartmentService.findById(id);
        if (aptOptional.isEmpty()) return ResponseEntity.notFound().build();
        Apartment apt = aptOptional.get();
        try {
            ApartmentDTO dto = new ApartmentDTO();
            dto.setId(apt.getId());
            dto.setPrice(apt.getPrice());
            dto.setAddress(apt.getAddress());
            dto.setParameters(apt.getParameters());
            dto.setDescription(apt.getDescription());
            dto.setLandlordId(apt.getLandlord().getId());
            JsonNode json = objectMapper.convertValue(dto, JsonNode.class);
            json = patch.apply(json);
            ApartmentDTO patchedDto = objectMapper.treeToValue(json, ApartmentDTO.class);

            return ResponseEntity.ok(patchedDto);
        } catch (JsonPatchException | JsonProcessingException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public void deleteById(@PathVariable Long id) {
        apartmentService.deleteById(id);
    }
}