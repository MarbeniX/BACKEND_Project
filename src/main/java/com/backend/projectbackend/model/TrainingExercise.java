package com.backend.projectbackend.model;

import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "trainingExercises")
public class TrainingExercise {
    @MongoId
    private ObjectId id;

    @NotNull
    private ObjectId exerciseId;
    @NotNull
    private Long timeToComplete;
    @NotNull
    private Integer setNumber;
    @NotNull
    private Integer reps;
    @NotNull
    private ObjectId trainingSessionId;
    @NotNull
    private ObjectId userId;

    public TrainingExercise() {}

    public TrainingExercise(ObjectId exerciseId, Long timeToComplete, Integer setNumber, Integer weight, Integer reps, ObjectId trainingSessionId, ObjectId userId) {
        this.timeToComplete = timeToComplete;
        this.setNumber = setNumber;
        this.reps = reps;
        this.trainingSessionId = trainingSessionId;
        this.userId = userId;
        this.exerciseId = exerciseId;
    }

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public ObjectId getExerciseId() { return exerciseId; }
    public void setExerciseId(ObjectId exerciseId) { this.exerciseId = exerciseId; }

    public Long getTimeToComplete() { return timeToComplete; }
    public void setTimeToComplete(Long timeToComplete) { this.timeToComplete = timeToComplete; }

    public Integer getSetNumber() { return setNumber; }
    public void setSetNumber(Integer setNumber) { this.setNumber = setNumber; }

    public Integer getReps() { return reps; }
    public void setReps(Integer reps) { this.reps = reps; }

    public ObjectId getTrainingSession() { return trainingSessionId; }
    public void setTrainingSession(ObjectId trainingSession) { this.trainingSessionId = trainingSession; }

    public ObjectId getUserId() { return userId; }
    public void setUserId(ObjectId userId) { this.userId = userId; }

}
