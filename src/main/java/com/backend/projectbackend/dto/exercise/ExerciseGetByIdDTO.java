package com.backend.projectbackend.dto.exercise;

import com.backend.projectbackend.model.Exercise;

public class ExerciseGetByIdDTO {
    private String id;
    private String title;
    private String description;
    private String imageURL;
    private String publicId;
    private Exercise.Muscle muscle;
    private Exercise.Difficulty difficulty;

    public ExerciseGetByIdDTO() {}

    public ExerciseGetByIdDTO(Exercise exercise) {
        this.id = exercise.getId().toHexString();
        this.title = exercise.getTitle();
        this.description = exercise.getDescription();
        this.imageURL = exercise.getImageURL();
        this.publicId = exercise.getPublicID();
        this.muscle = exercise.getMuscle();
        this.difficulty = exercise.getDifficulty();
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImageURL() { return imageURL; }
    public String getPublicId() { return publicId; }
    public Exercise.Muscle getMuscle() { return muscle; }
    public Exercise.Difficulty getDifficulty() { return difficulty; }
}
