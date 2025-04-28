package com.backend.projectbackend.repository;

import com.backend.projectbackend.model.User;
import org.springframework.stereotype.Repository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@Repository
public interface AuthRepository extends MongoRepository<User, ObjectId> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}