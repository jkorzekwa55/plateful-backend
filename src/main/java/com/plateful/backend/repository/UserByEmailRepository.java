package com.plateful.backend.repository;

import com.plateful.backend.entity.UserByEmail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserByEmailRepository extends CrudRepository<UserByEmail, UUID> {
    Optional<UserByEmail> findByEmail(String email);
}
