package org.alonso.blogapp.controllers;

import static org.alonso.blogapp.models.dto.validations.Validator.validate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alonso.blogapp.models.dto.CreateUserDTO;
import org.alonso.blogapp.models.dto.UpdateUserDTO;
import org.alonso.blogapp.models.dto.UserDTO;
import org.alonso.blogapp.models.services.UserService;
import org.alonso.blogapp.models.services.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> index() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("users", userService.findAll());

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping
    public ResponseEntity<?> store(@Valid @RequestBody CreateUserDTO createUserDTO, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        List<String> errors = validate(result);

        if (!errors.isEmpty()) {
            response.put("status", 400);
            response.put("errors", errors);

            return ResponseEntity.status(400).body(response);
        }

        response.put("status", 201);
        response.put("user", userService.save(createUserDTO));

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        UserDTO user = userService.findById(id);
        if (user == null) {
            log.error("No existe un usuario con id " + id);
            response.put("status", 404);
            response.put("error", "No existe un usuario con id " + id);

            return ResponseEntity.status(404).body(response);
        }

        response.put("status", 200);
        response.put("user", user);

        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateUserDTO updateUserDTO, BindingResult result,
            @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        List<String> errors = validate(result);

        if (!errors.isEmpty()) {
            response.put("status", 400);
            response.put("errors", errors);

            return ResponseEntity.status(400).body(response);
        }

        UserDTO user = null;

        try {
            user = userService.update(updateUserDTO, id);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            response.put("status", 404);
            response.put("error", e.getMessage());

            return ResponseEntity.status(404).body(response);
        }

        response.put("status", 200);
        response.put("message", "El id actualizado es el " + id);
        response.put("user", user);

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        UserDTO user = null;

        try {
            user = userService.delete(id);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            response.put("status", 404);
            response.put("error", e.getMessage());

            return ResponseEntity.status(404).body(response);
        }

        response.put("status", 200);
        response.put("message", "El id eliminado es el " + id);
        response.put("user", user);

        return ResponseEntity.status(200).body(response);
    }
}
