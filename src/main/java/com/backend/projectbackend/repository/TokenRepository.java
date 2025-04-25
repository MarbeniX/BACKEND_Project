package com.backend.projectbackend.repository;

import com.backend.projectbackend.model.Token;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends MongoRepository<Token, ObjectId> {

    // Puedes agregar métodos personalizados si los necesitas, por ejemplo:
    Token findByUserId(ObjectId userId);

    Token findByTokenValue(String token);

}