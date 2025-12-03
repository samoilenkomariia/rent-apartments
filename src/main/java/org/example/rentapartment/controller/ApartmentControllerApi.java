package org.example.rentapartment.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletRequest;
import org.example.rentapartment.model.apartment.Apartment;
import org.example.rentapartment.model.apartment.ApartmentDTO;
import org.example.rentapartment.model.apartment.ApartmentSearchDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collection;

@Tag(name = "Apartment", description = "Apartment operations")
public interface ApartmentControllerApi {
    @GetMapping
    @Operation(
            summary = "Get All Apartments",
            description = "Get a list of all available apartments."
    )
    ResponseEntity<Collection<ApartmentDTO>> getAll();

    @GetMapping("/{id}")
    @Operation(
            summary = "Get Apartment By Id",
            description = "Retrieve details of a specific apartment by ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Apartment found"),
            @ApiResponse(responseCode = "404", description = "Apartment not found", content = @Content)
    })
    ResponseEntity<ApartmentDTO> getById(@PathVariable Long id);

    @PostMapping
    @Operation(
            summary = "Create Apartment",
            description = "Create a new apartment, requires a valid landlord ID."

    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Apartment successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    })
    ResponseEntity<ApartmentDTO> create(@RequestBody ApartmentDTO dto);

    @PutMapping("/{id}")
    @Operation(
            summary = "Update Apartment",
            description = "Fully update an existing apartment, all fields must be provided."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Apartment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request: ID mismatch or invalid data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Apartment or Landlord not found", content = @Content)
    })
    ResponseEntity<ApartmentDTO> update(@PathVariable Long id, @RequestBody ApartmentDTO aptDto);

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    @Operation(
            summary = "Patch Apartment Json Patch",
            description = "Update specific fields of an apartment using RFC 6902 JSON Patch format."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Apartment patched successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Invalid patch format", content = @Content),
            @ApiResponse(responseCode = "404", description = "Apartment not found", content = @Content)
    })
    ResponseEntity<ApartmentDTO> jsonPatch(@PathVariable Long id, @RequestBody JsonPatch patch);

    @PatchMapping(path = "/{id}", consumes = "application/merge-patch+json")
    @Operation(
            summary = "Patch Apartment Merge Patch",
            description = "Update specific fields of an apartment using RFC 7386 JSON Merge Patch format."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Apartment patched successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Invalid patch format", content = @Content),
            @ApiResponse(responseCode = "404", description = "Apartment not found", content = @Content)
    })
    ResponseEntity<ApartmentDTO> jsonMergePatch(@PathVariable Long id, @RequestBody JsonMergePatch patch);

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete Apartment",
            description = "Permanently remove an apartment"
    )
    void deleteById(@PathVariable Long id);

    @GetMapping("/search/byRegion")
    @Operation(
            summary = "Search Apartments by Region",
            description = "Filter apartments that are located in a specific region"
    )
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    ResponseEntity<Collection<ApartmentDTO>> searchByRegion(
            @Parameter(description = "DTO containing the region filter", required = true)
            @ModelAttribute ApartmentSearchDTO searchDTO);

    @GetMapping("/search/byCity")
    @Operation(
            summary = "Search Apartments by City",
            description = "Filter apartments by city"
    )
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    ResponseEntity<Collection<ApartmentDTO>> searchByCity(
            @Parameter(description = "DTO containing the city filter", required = true)
            @ModelAttribute ApartmentSearchDTO searchDTO);

    @GetMapping("/search/byPrice")
    @Operation(
            summary = "Search Apartments by Price",
            description = "Find apartments cheaper than the specified price"
    )
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    ResponseEntity<Collection<ApartmentDTO>> searchByPrice(
            @Parameter(description = "Maximum price threshold", required = true)
            @RequestParam BigDecimal price);
}

