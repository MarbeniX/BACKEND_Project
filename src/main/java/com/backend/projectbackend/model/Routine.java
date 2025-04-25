package com.backend.projectbackend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "routines")
public class Routine {
    public enum Category{
        PULL,
        PUSH,
        LEG,
        CHEST,
        BACK,
        CORE,
        FREE
    }

    @MongoId
    private ObjectId id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    @Size(min = 1, max = 200)
    private String description;

    @CreatedDate
    private Date creationDate;

    @DBRef
    private List<Exercise> exercises;

    private ObjectId userId;
    private Category category = Category.FREE;

    public Routine() {}

    public Routine(String name, String description) {
        this.name = name;
        this.description = description;
        this.exercises = new ArrayList<>();
    }

    //Getters and setters
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

    public List<Exercise> getExercises() { return exercises; }
    public void setExercises(List<Exercise> exercises) { this.exercises = exercises; }

    public ObjectId getUserId() { return userId; }
    public void setUserId(ObjectId userId) { this.userId = userId; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    @Override
    public String toString() {
        return "Routine{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
