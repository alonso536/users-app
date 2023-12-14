package org.alonso.blogapp.models.services;

import java.util.List;

import org.alonso.blogapp.models.dto.CreateUserDTO;
import org.alonso.blogapp.models.dto.UpdateUserDTO;
import org.alonso.blogapp.models.dto.UserDTO;

public interface UserService {
    List<UserDTO> findAll();

    UserDTO save(CreateUserDTO createUserDTO);

    UserDTO findById(Long id);

    UserDTO update(UpdateUserDTO updateUserDTO, Long id);

    UserDTO delete(Long id);
}
