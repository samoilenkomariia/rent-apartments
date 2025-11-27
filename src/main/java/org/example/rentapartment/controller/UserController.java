package org.example.rentapartment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.Patch;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.example.rentapartment.model.User;
import org.example.rentapartment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController implements UserControllerApi {
    private UserService userService;
    private ObjectMapper objectMapper;

    @Autowired
    public UserController(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    @Override
    public ResponseEntity<Collection<User>> getAllUsers() {
        Collection<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Override
    public ResponseEntity<User> create(@RequestBody User user) {
        try {
            User newUser = userService.save(user);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                    .path("/{id}").buildAndExpand(newUser.getId()).toUri();
            return ResponseEntity.created(uri).body(newUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User userDto) {
        if (userDto.getId() != null && !userDto.getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            User updated = userService.update(id, userDto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    @Override
    public ResponseEntity<User> jsonPatch(@PathVariable Long id,
                                          @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Json Patch operations array",
                                                  content = @Content(schema = @Schema(implementation = JsonPatch.class)))
                                          @RequestBody JsonPatch patch) {
        return patch(id, patch);
    }

    @PatchMapping(path = "/{id}", consumes = "application/merge-patch+json")
    @Override
    public ResponseEntity<User> jsonMergePatch(@PathVariable Long id,
                                               @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "JSON Merge Patch object",
                                                       content = @Content(schema = @Schema(implementation = JsonMergePatch.class)))
                                               @RequestBody JsonMergePatch patch) {
        return patch(id, patch);
    }

    private ResponseEntity<User> patch(Long id, Patch patch) {
        Optional<User> apt = userService.findById(id);
        if (apt.isEmpty()) return ResponseEntity.notFound().build();
        try {
            JsonNode json = objectMapper.convertValue(apt.get(), JsonNode.class);
            json = patch.apply(json);
            User newUser = objectMapper.treeToValue(json, User.class);
            newUser = userService.update(id, newUser);
            return ResponseEntity.ok(newUser);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Override
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}