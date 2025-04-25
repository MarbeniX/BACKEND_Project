package com.backend.projectbackend.repository;

import com.backend.projectbackend.model.Routine;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineRepository extends MongoRepository<Routine, ObjectId> {
}
