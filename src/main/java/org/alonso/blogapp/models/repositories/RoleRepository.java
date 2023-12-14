package org.alonso.blogapp.models.repositories;

import org.alonso.blogapp.models.entities.Role;

import org.alonso.blogapp.models.dto.enums.ERole;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(ERole name);
}
