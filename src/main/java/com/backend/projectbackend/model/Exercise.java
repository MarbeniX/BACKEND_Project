package com.backend.projectbackend.model;

import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "exercises")
public class Exercise {
    public enum Muscle{
        SHOULDER("#001219"),
        BICEP("#005f73"),
        TRICEP("#0a9396"),
        FOREARM("#94d2bd"),
        CHEST("#e9d8a6"),
        BACK("#ee9b00"),
        LEG("#ca6702"),
        ASS("#bb3e03"),
        CORE("#ae2012"),
        OTHER("#9b2226");

        private String color;
        Muscle(String color) { this.color = color; }
        public String getColor() { return color; }
    }

    public enum Difficulty{
        BEGINNER1("#ffb3c1"),
        BEGINNER2("#ff8fa3"),
        BEGINNER3("#ff758f"),
        INTERMEDIATE1("#90caf9"),
        INTERMEDIATE2("#64b5f6"),
        INTERMEDIATE3("#42a5f5"),
        ADVANCED1("#e0aaff"),
        ADVANCED2("#c77dff"),
        ADVANCED3("#9d4edd"),
        OPEN("#dceab2");

        private final String color;
        Difficulty(String color) { this.color = color; }
        public String getColor() { return color; }
    }

    @MongoId
    private ObjectId id;

    @Size(max = 250)
    private String description;
    private String title;
    private Muscle muscle;
    private Difficulty difficulty;
    private String imageURL;
    private String publicID;

    public Exercise() {}

    public Exercise(String title, Muscle muscle, String description, Difficulty difficulty, String imageURL, String publicID) {
        this.title = title;
        this.muscle = muscle;
        this.description = description;
        this.difficulty = difficulty;
        this.imageURL = imageURL;
        this.publicID = publicID;
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

    public String getImageURL() { return imageURL; }
    public void setImageURL(String imageURL) { this.imageURL = imageURL; }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }
}
