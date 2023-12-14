package org.alonso.blogapp.models.repositories;

import java.util.Optional;

import org.alonso.blogapp.models.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
