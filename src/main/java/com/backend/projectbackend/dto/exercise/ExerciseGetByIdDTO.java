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
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageURL() { return imageURL; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }

    public String getPublicId() { return publicId; }
    public void setPublicId(String publicId) { this.publicId = publicId; }

    public Exercise.Muscle getMuscle() { return muscle; }
    public void setMuscle(Exercise.Muscle muscle) { this.muscle = muscle; }

    public Exercise.Difficulty getDifficulty() { return difficulty; }
    public void setDifficulty(Exercise.Difficulty difficulty) { this.difficulty = difficulty; }
}
