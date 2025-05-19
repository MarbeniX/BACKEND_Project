package com.backend.projectbackend.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;
import java.util.List;

@Document(collection = "trainingSessions")
public class TrainingSession {
    @MongoId
    private ObjectId id;
    @DBRef
    private List<TrainingExercise> exercises;
    @CreatedDate
    private Date trainingDate;
    private ObjectId userId;
    private ObjectId routineId;

    public TrainingSession() {}

    public TrainingSession(List<TrainingExercise> trainingExercisesList, ObjectId userId, ObjectId routineId) {
        this.exercises = trainingExercisesList;
        this.userId = userId;
        this.routineId = routineId;
    }

    //Getters and setters
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public List<TrainingExercise> getExercises() { return exercises; }
    public void setExercises(List<TrainingExercise> exercises) { this.exercises = exercises; }

    public Date getTrainingDate() { return trainingDate; }
    public void setTrainingDate(Date trainingDate) { this.trainingDate = trainingDate; }

    public ObjectId getUserId() { return userId; }
    public void setUserId(ObjectId userId) { this.userId = userId; }

    public ObjectId getRoutineId() { return routineId; }
    public void setRoutineId(ObjectId routineId) { this.routineId = routineId; }
}
