package org.alonso.blogapp.models.services;

import static org.alonso.blogapp.models.dto.helpers.UserHelper.filterRoles;
import static org.alonso.blogapp.models.dto.helpers.UserHelper.buildUser;
import static org.alonso.blogapp.models.dto.helpers.UserHelper.getFieldError;
import static org.alonso.blogapp.models.dto.helpers.UserHelper.updateUser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.alonso.blogapp.models.dto.CreateUserDTO;
import org.alonso.blogapp.models.dto.UpdatePasswordDTO;
import org.alonso.blogapp.models.dto.UpdateUserDTO;
import org.alonso.blogapp.models.dto.UserDTO;
import org.alonso.blogapp.models.entities.Region;
import org.alonso.blogapp.models.entities.Role;
import org.alonso.blogapp.models.entities.UserEntity;
import org.alonso.blogapp.models.repositories.RegionRepository;
import org.alonso.blogapp.models.repositories.RoleRepository;
import org.alonso.blogapp.models.repositories.UserRepository;
import org.alonso.blogapp.models.services.exceptions.FieldUniqueException;
import org.alonso.blogapp.models.services.exceptions.InvalidPasswordException;
import org.alonso.blogapp.models.services.exceptions.RegionNotFoundException;
import org.alonso.blogapp.models.services.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        List<UserEntity> users = (List<UserEntity>) userRepository.getAll();

        return users.stream()
                .map((user) -> buildUser(user))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAll(Pageable page) {
        return userRepository.getAll(page)
                .stream()
                .map((user) -> buildUser(user))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO save(CreateUserDTO createUserDTO) {
        List<Role> roles = filterRoles(createUserDTO.getRoles(), roleRepository);
        Region region = regionRepository.findById(createUserDTO.getRegion())
                .orElseThrow(() -> new RegionNotFoundException("La región ingresada no es válida"));

        UserEntity user = UserEntity.builder()
                .firstname(createUserDTO.getFirstname())
                .lastname(createUserDTO.getLastname())
                .username(createUserDTO.getUsername())
                .email(createUserDTO.getEmail())
                .password(passwordEncoder.encode(createUserDTO.getPassword()))
                .phone(createUserDTO.getPhone())
                .birthdate(createUserDTO.getBirthdate())
                .region(region)
                .roles(roles)
                .build();

        UserEntity userEntity = null;

        try {
            userEntity = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new FieldUniqueException(getFieldError(e.getMessage()), "se encuentra en uso");
        }

        return buildUser(userEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findOne(String term) {
        Optional<UserEntity> optionalUser = userRepository.getById(term);
        if (!optionalUser.isPresent()) {
            optionalUser = userRepository.getBySlug(term);
            if (!optionalUser.isPresent()) {
                return null;
            }
        }

        return buildUser(optionalUser.orElseThrow());
    }

    @Override
    @Transactional
    public UserDTO update(UpdateUserDTO updateUserDTO, String id) {
        UserEntity user = userRepository.getById(id)
                .orElseThrow(() -> new UserNotFoundException("No existe un usuario con el id " + id));

        Region region = regionRepository.findById(updateUserDTO.getRegion())
                .orElseThrow(() -> new RegionNotFoundException("La región ingresada no es válida"));

        user = updateUser(updateUserDTO, user);
        user.setRegion(region);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new FieldUniqueException(getFieldError(e.getMessage()), "se encuentra en uso");
        }

        return buildUser(user);
    }

    @Override
    @Transactional
    public void updatePassword(UpdatePasswordDTO updatePasswordDTO, String id) {
        UserEntity user = userRepository.getById(id)
                .orElseThrow(() -> new UserNotFoundException("No existe un usuario con el id " + id));

        if (!passwordEncoder.matches(updatePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("La contraseña ingresada no coincide con la original");
        }

        user.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDTO delete(String id) {
        UserEntity user = userRepository.getById(id)
                .orElseThrow(() -> new UserNotFoundException("No existe un usuario con el id " + id));

        user.setActive(false);
        return buildUser(userRepository.save(user));
    }
}