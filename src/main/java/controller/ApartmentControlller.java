package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.github.fge.jsonpatch.Patch;
import jakarta.servlet.ServletRequest;
import model.apartment.Apartment;
import model.apartment.ApartmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import service.ApartmentService;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

@RequestMapping("apartments")
@Controller
public class ApartmentControlller {
    private ApartmentService apartmentService;
    private ObjectMapper objectMapper;

    @Autowired
    public ApartmentControlller(ApartmentService apartmentService,  ObjectMapper objectMapper) {
        this.apartmentService = apartmentService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<Collection<Apartment>> getAll() {
        return ResponseEntity.ok(apartmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Apartment> getById(@PathVariable Long id, ServletRequest servletRequest) {
        return apartmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Apartment> create(@RequestBody ApartmentDTO dto) {
        Apartment newApart = apartmentService.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}").buildAndExpand(newApart.getId()).toUri();
        return ResponseEntity.created(uri).body(newApart);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Apartment> update(@PathVariable Long id, @RequestBody ApartmentDTO aptDto) {
        if (aptDto.getId() != null && !aptDto.getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Apartment updated = apartmentService.update(id, aptDto);
            return  ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Apartment> jsonPatch(@PathVariable Long id, @RequestBody JsonPatch patch) {
        return patch(id, patch);
    }

    @PatchMapping(path = "/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Apartment> jsonMergePatch(@PathVariable Long id, @RequestBody JsonMergePatch patch) {
        return patch(id, patch);
    }

    private ResponseEntity<Apartment> patch(Long id, Patch patch) {
        Optional<Apartment> aptOptional = apartmentService.findById(id);
        if (aptOptional.isEmpty()) return ResponseEntity.notFound().build();
        Apartment apt = aptOptional.get();
        try {
            JsonNode json = objectMapper.convertValue(apt, JsonNode.class);
            json = patch.apply(json);
            Apartment newApt = objectMapper.treeToValue(json, Apartment.class);
            newApt = apartmentService.save(newApt);
            return ResponseEntity.ok(newApt);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        apartmentService.deleteById(id);
    }
}
