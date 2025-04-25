package com.backend.projectbackend.model;

import jakarta.validation.constraints.NotBlank;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Document(collection = "friends")
public class Friend {
    @MongoId
    private ObjectId id;

    @NotBlank
    private String name;

    @CreatedDate
    private Date createdAt;

    public Friend() {}

    public Friend(String name) {
        this.name = name;
    }

    //Getters and setters
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }


}
