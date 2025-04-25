package com.backend.projectbackend.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;


@Document(collection = "tokens")
public class Token {
    @MongoId
    private ObjectId id;

    private ObjectId userId;

    @CreatedDate
    private Date tokenDate;

    private String tokenValue;

    public Token() {}

    public Token(ObjectId userId, String tokenValue) {
        this.userId = userId;
        this.tokenValue = tokenValue;
    }

    //Getters and setters

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public ObjectId getUserId() { return userId; }
    public void setUserId(ObjectId userId) { this.userId = userId; }

    public String getTokenValue() { return tokenValue; }
    public void setTokenValue(String tokenValue) { this.tokenValue = tokenValue; }
}
