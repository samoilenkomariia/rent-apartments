package org.example.rentapartment.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.rentapartment.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Tag(name = "User", description = "User operations")
public interface UserControllerApi {

    @GetMapping
    @Operation(
            summary = "Get All Users",
            description = "Retrieve a list of all users."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    ResponseEntity<Collection<User>> getAllUsers();

    @GetMapping("/{id}")
    @Operation(
            summary = "Get User By Id",
            description = "Retrieve specific user by ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    ResponseEntity<User> getById(@PathVariable Long id);

    @PostMapping
    @Operation(
            summary = "Create User",
            description = "Register a new user in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Invalid input", content = @Content)
    })
    ResponseEntity<User> create(@RequestBody User user);

    @PutMapping("/{id}")
    @Operation(
            summary = "Update User",
            description = "Fully update an existing user's information."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request: ID mismatch or invalid data", content = @Content)
    })
    ResponseEntity<User> update(@PathVariable Long id, @RequestBody User userDto);

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    @Operation(
            summary = "Patch User Json Patch",
            description = "Update specific fields of a user using RFC 6902 JSON Patch format."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User patched successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Invalid patch format", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    ResponseEntity<User> jsonPatch(@PathVariable Long id,
                                   @RequestBody
                                   @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Json Patch operations array",
                                           content = @Content(schema = @Schema(implementation = JsonPatch.class)))
                                   JsonPatch patch);

    @PatchMapping(path = "/{id}", consumes = "application/merge-patch+json")
    @Operation(
            summary = "Patch User Merge Patch",
            description = "Update specific fields of a user using RFC 7386 JSON Merge Patch format."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User patched successfully"),
            @ApiResponse(responseCode = "400", description = "Bad Request: Invalid patch format", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    ResponseEntity<User> jsonMergePatch(@PathVariable Long id,
                                        @RequestBody
                                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "JSON Merge Patch object",
                                                content = @Content(schema = @Schema(implementation = JsonMergePatch.class)))
                                        JsonMergePatch patch);

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete User",
            description = "Permanently remove a user from the system."
    )
    void deleteById(@PathVariable Long id);
}