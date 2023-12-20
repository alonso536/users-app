package org.alonso.blogapp.models.services;

import java.util.List;

import org.alonso.blogapp.models.dto.CreateUserDTO;
import org.alonso.blogapp.models.dto.UpdatePasswordDTO;
import org.alonso.blogapp.models.dto.UpdateUserDTO;
import org.alonso.blogapp.models.dto.UserDTO;
import org.alonso.blogapp.models.entities.Region;
import org.springframework.data.domain.Pageable;

public interface UserService {
    List<UserDTO> findAll();

    List<UserDTO> findAll(Pageable page);

    UserDTO save(CreateUserDTO createUserDTO);

    UserDTO findOne(String term);

    UserDTO findByUsername(String username);

    UserDTO update(UpdateUserDTO updateUserDTO, String id);

    void updatePassword(UpdatePasswordDTO updatePasswordDTO, String id);

    UserDTO delete(String id);

    List<Region> findRegions();
}
