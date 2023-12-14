package org.alonso.blogapp.models.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.alonso.blogapp.models.dto.CreateUserDTO;
import org.alonso.blogapp.models.dto.UpdateUserDTO;
import org.alonso.blogapp.models.dto.UserDTO;
import org.alonso.blogapp.models.entities.Role;
import org.alonso.blogapp.models.entities.UserEntity;
import org.alonso.blogapp.models.dto.enums.ERole;
import org.alonso.blogapp.models.repositories.RoleRepository;
import org.alonso.blogapp.models.repositories.UserRepository;
import org.alonso.blogapp.models.services.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> findAll() {
        List<UserEntity> users = (List<UserEntity>) userRepository.findAll();

        return users.stream()
                .filter((user) -> user.getActive())
                .map((user) -> buildUser(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO save(CreateUserDTO createUserDTO) {
        List<Role> roles = filterRoles(createUserDTO.getRoles());

        UserEntity user = UserEntity.builder()
                .username(createUserDTO.getUsername())
                .email(createUserDTO.getEmail())
                .password(passwordEncoder.encode(createUserDTO.getPassword()))
                .roles(roles)
                .build();

        return buildUser(userRepository.save(user));
    }

    @Override
    public UserDTO findById(Long id) {
        Optional<UserEntity> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return null;
        }

        UserEntity user = optionalUser.orElseThrow();
        return (user.getActive()) ? buildUser(user) : null;
    }

    @Override
    public UserDTO update(UpdateUserDTO updateUserDTO, Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No existe un usuario con id " + id));

        if (!user.getActive()) {
            throw new UserNotFoundException("No existe un usuario con id " + id);
        }

        user.setUsername(updateUserDTO.getUsername());
        user.setEmail(updateUserDTO.getEmail());

        return buildUser(userRepository.save(user));
    }

    @Override
    public UserDTO delete(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No existe un usuario con id " + id));

        if (!user.getActive()) {
            throw new UserNotFoundException("No existe un usuario con id " + id);
        }

        user.setActive(false);
        return buildUser(userRepository.save(user));
    }

    private List<Role> filterRoles(List<String> roles) {
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

    private UserDTO buildUser(UserEntity user) {
        List<String> roles = user.getRoles()
                .stream()
                .map((role) -> role.getName().toString())
                .collect(Collectors.toList());

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .active(user.getActive())
                .roles(roles)
                .build();
    }
}
