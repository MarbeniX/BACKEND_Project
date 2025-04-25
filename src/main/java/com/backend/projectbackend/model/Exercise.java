package com.backend.projectbackend.model;

import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "exercises")
public class Exercise {
    public enum Muscle{
        SHOULDER,
        BICEP,
        TRICEPS,
        CHEST,
        BACK,
        LAT,
        LEG,
        OTHER
    }

    public enum Difficulty{
        BEGINNER1,
        BEGINNER2,
        BEGINNER3,
        INTERMEDIATE1,
        INTERMEDIATE2,
        INTERMEDIATE3,
        ADVANCED1,
        ADVANCED2,
        ADVANCED3,
        OPEN
    }

    @MongoId
    private ObjectId id;

    @Size(max = 250)
    private String description;
    private String title;
    private Muscle muscle = Muscle.OTHER;
    private Difficulty difficulty = Difficulty.OPEN;

    public Exercise() {}

    public Exercise(String title, Muscle muscle, String description, Difficulty difficulty) {
        this.title = title;
        this.muscle = muscle;
        this.description = description;
        this.difficulty = difficulty;
    }

    //Getters and setters

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Muscle getMuscle() { return muscle; }
    public void setMuscle(Muscle muscle) { this.muscle = muscle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    @Override
    public String toString() {
        return "Exercise{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", muscle=" + muscle +
                ", description='" + description + '\'' +
                '}';
    }

}
