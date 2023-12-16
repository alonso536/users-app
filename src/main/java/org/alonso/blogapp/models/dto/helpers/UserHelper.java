package org.alonso.blogapp.models.dto.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.alonso.blogapp.models.dto.UpdateUserDTO;
import org.alonso.blogapp.models.dto.UserDTO;
import org.alonso.blogapp.models.dto.enums.ERole;
import org.alonso.blogapp.models.entities.Role;
import org.alonso.blogapp.models.entities.UserEntity;
import org.alonso.blogapp.models.repositories.RoleRepository;
import org.springframework.validation.BindingResult;

public class UserHelper {

    public static Map<String, String> validate(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        if (result.hasErrors()) {
            result.getFieldErrors()
                    .forEach((error) -> {
                        if (!errors.containsKey(error.getField())) {
                            errors.put(error.getField(),
                                    "El campo " + error.getField() + " " + error.getDefaultMessage());
                        }
                    });
        }

        return errors;
    }

    public static List<Role> filterRoles(List<String> roles, RoleRepository roleRepository) {
        List<Role> rolesUser = new ArrayList<>();

        try {
            if (roles == null || roles.isEmpty()) {
                rolesUser.add(roleRepository.findByName(ERole.valueOf("ROLE_USER")));
            } else {
                rolesUser = roles.stream()
                        .map((role) -> roleRepository.findByName(ERole.valueOf(role)))
                        .collect(Collectors.toList());
            }
        } catch (IllegalArgumentException e) {
            return Arrays.asList(roleRepository.findByName(ERole.valueOf("ROLE_USER")));
        }

        return rolesUser;
    }

    public static UserDTO buildUser(UserEntity user) {
        List<String> roles = user.getRoles()
                .stream()
                .map((role) -> role.getName().toString())
                .collect(Collectors.toList());

        return UserDTO.builder()
                .id(user.getId())
                .name(user.getFirstname() + " " + user.getLastname())
                .username(user.getUsername())
                .slug(user.getSlug())
                .email(user.getEmail())
                .phone(user.getPhone())
                .birthdate(user.getBirthdate())
                .region(user.getRegion().getName())
                .avatar(user.getAvatar())
                .active(user.getActive())
                .roles(roles)
                .build();
    }

    public static String getFieldError(String message) {
        String field = null;
        if (message.contains("uk_username")) {
            field = "username";
        } else if (message.contains("uk_email")) {
            field = "email";
        } else if (message.contains("uk_slug")) {
            field = "slug";
        }

        return field;
    }

    public static UserEntity updateUser(UpdateUserDTO updateUserDTO, UserEntity user) {
        user.setFirstname(updateUserDTO.getFirstname());
        user.setLastname(updateUserDTO.getLastname());
        user.setUsername(updateUserDTO.getUsername());
        user.setSlug(updateUserDTO.getSlug());
        user.setPhone(updateUserDTO.getPhone());
        user.setBirthdate(updateUserDTO.getBirthdate());

        return user;
    }
}
