package org.alonso.blogapp.models.repositories;

import java.util.List;
import java.util.Optional;

import org.alonso.blogapp.models.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository
        extends CrudRepository<UserEntity, String>, PagingAndSortingRepository<UserEntity, String> {

    @Query("SELECT u FROM UserEntity u WHERE u.active = true AND confirmed = true")
    List<UserEntity> getAll();

    @Query("SELECT u FROM UserEntity u WHERE u.active = true AND confirmed = true")
    Page<UserEntity> getAll(Pageable page);

    @Query("SELECT u FROM UserEntity u WHERE u.id = :id AND u.active = true AND confirmed = true")
    Optional<UserEntity> getById(@Param("id") String id);

    @Query("SELECT u FROM UserEntity u WHERE u.username = :username AND u.active = true AND confirmed = true")
    Optional<UserEntity> getByUsername(@Param("username") String username);

    @Query("SELECT u FROM UserEntity u WHERE u.slug = :slug AND u.active = true AND confirmed = true")
    Optional<UserEntity> getBySlug(@Param("slug") String slug);
}
