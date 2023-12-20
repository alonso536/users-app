package org.alonso.blogapp.controllers;

import static org.alonso.blogapp.models.dto.helpers.UserHelper.validate;

import java.util.HashMap;
import java.util.Map;

import org.alonso.blogapp.models.dto.CreateUserDTO;
import org.alonso.blogapp.models.dto.UpdateUserDTO;
import org.alonso.blogapp.models.dto.UpdatePasswordDTO;
import org.alonso.blogapp.models.dto.UserDTO;
import org.alonso.blogapp.models.services.UserService;
import org.alonso.blogapp.models.services.exceptions.FieldUniqueException;
import org.alonso.blogapp.models.services.exceptions.InvalidPasswordException;
import org.alonso.blogapp.models.services.exceptions.RegionNotFoundException;
import org.alonso.blogapp.models.services.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
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

    @GetMapping("/page/{page}")
    public ResponseEntity<?> index(@PathVariable Integer page) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("users", userService.findAll(PageRequest.of(page, 2)));

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping
    public ResponseEntity<?> store(@Valid @RequestBody CreateUserDTO createUserDTO, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = validate(result);

        String confirmPassword = (createUserDTO.getConfirmPassword() != null) ? createUserDTO.getConfirmPassword() : "";

        if (createUserDTO.getPassword() != null
                && !createUserDTO.getPassword().equals(confirmPassword)) {
            errors.put("confirmPassword", "Las contraseñas deben coincidir");
        }

        if (!errors.isEmpty()) {
            response.put("status", 400);
            response.put("errors", errors);

            return ResponseEntity.status(400).body(response);
        }

        UserDTO user = null;

        try {
            user = userService.save(createUserDTO);
        } catch (Exception e) {
            response.put("status", 400);
            if (e instanceof RegionNotFoundException) {
                response.put("errors", Map.of("region", e.getMessage()));
            } else if (e instanceof FieldUniqueException) {
                String field = ((FieldUniqueException) e).getField();
                response.put("errors",
                        Map.of(field, "El campo " + field + " " + e.getMessage()));
            }

            return ResponseEntity.status(400).body(response);
        }

        response.put("status", 201);
        response.put("user", user);

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{term}")
    public ResponseEntity<?> show(@PathVariable String term) {
        Map<String, Object> response = new HashMap<>();

        UserDTO user = userService.findOne(term);
        if (user == null) {
            log.error("No existe un usuario con el término " + term);
            response.put("status", 404);
            response.put("errors", Map.of("id", "No existe un usuario con el término " + term));

            return ResponseEntity.status(404).body(response);
        }

        response.put("status", 200);
        response.put("user", user);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/check-auth/{username}")
    public ResponseEntity<?> checkAuth(HttpServletRequest request, @PathVariable String username) {
        Map<String, Object> response = new HashMap<>();

        UserDTO user = userService.findByUsername(username);
        if (user == null) {
            log.error("No existe un usuario con el username " + username);
            response.put("status", 404);
            response.put("errors", Map.of("id", "No existe un usuario con el username " + username));
            return ResponseEntity.status(404).body(response);
        }

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            response.put("token", token);
        }

        response.put("status", 200);
        response.put("user", user);

        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody UpdateUserDTO updateUserDTO, BindingResult result,
            @PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = validate(result);

        if (!errors.isEmpty()) {
            response.put("status", 400);
            response.put("errors", errors);

            return ResponseEntity.status(400).body(response);
        }

        UserDTO user = null;

        try {
            user = userService.update(updateUserDTO, id);
        } catch (Exception e) {
            if (e instanceof UserNotFoundException) {
                response.put("status", 404);
                response.put("errors", Map.of("id", e.getMessage()));

                return ResponseEntity.status(404).body(response);
            } else if (e instanceof RegionNotFoundException) {
                response.put("status", 400);
                response.put("errors", Map.of("region", e.getMessage()));
            } else if (e instanceof FieldUniqueException) {
                String field = ((FieldUniqueException) e).getField();
                response.put("status", 400);
                response.put("errors",
                        Map.of(field, "El campo " + field + " " + e.getMessage()));
            }

            return ResponseEntity.status(400).body(response);
        }

        response.put("status", 200);
        response.put("message", "El id actualizado es el " + id);
        response.put("user", user);

        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordDTO updatePasswordDTO,
            BindingResult result, @PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = validate(result);

        String confirmPassword = (updatePasswordDTO.getConfirmNewPassword() != null)
                ? updatePasswordDTO.getConfirmNewPassword()
                : "";

        if (updatePasswordDTO.getNewPassword() != null
                && !updatePasswordDTO.getNewPassword().equals(confirmPassword)) {
            errors.put("confirmNewPassword", "Las contraseñas deben coincidir");
        }

        if (!errors.isEmpty()) {
            response.put("status", 400);
            response.put("errors", errors);

            return ResponseEntity.status(400).body(response);
        }

        try {
            userService.updatePassword(updatePasswordDTO, id);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            response.put("status", 404);
            response.put("errors", Map.of("id", e.getMessage()));

            return ResponseEntity.status(404).body(response);
        } catch (InvalidPasswordException e) {
            log.error(e.getMessage());
            response.put("status", 400);
            response.put("errors", Map.of("currentPassword", e.getMessage()));

            return ResponseEntity.status(400).body(response);
        }

        response.put("status", 200);
        response.put("message", "Contraseña actualizada exitosamente");
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        UserDTO user = null;

        try {
            user = userService.delete(id);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            response.put("status", 404);
            response.put("errors", Map.of("id", e.getMessage()));

            return ResponseEntity.status(404).body(response);
        }

        response.put("status", 200);
        response.put("message", "El id eliminado es el " + id);
        response.put("user", user);

        return ResponseEntity.status(200).body(response);
    }
}
