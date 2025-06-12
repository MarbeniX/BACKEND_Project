package com.backend.projectbackend.dto.exercise;

import com.backend.projectbackend.model.Exercise.Muscle;
import com.backend.projectbackend.model.Exercise.Difficulty;
import org.springframework.web.multipart.MultipartFile;

public class ExerciseCreateDTO {
    private String description;
    private String title;
    private Muscle muscle;
    private Difficulty difficulty;
    private String imageURL;
    private String publicID;
    private MultipartFile image;

    public ExerciseCreateDTO() {}

    public ExerciseCreateDTO(String description, String title, Muscle muscle, Difficulty difficulty, String imageURL, String publicID, MultipartFile image) {
        this.description = description;
        this.title = title;
        this.muscle = muscle;
        this.difficulty = difficulty;
        this.imageURL = imageURL;
        this.publicID = publicID;
        this.image = image;
    }

    //Setters and getters
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public Muscle getMuscle() {return muscle;}
    public void setMuscle(Muscle muscle) {this.muscle = muscle;}

    public Difficulty getDifficulty() {return difficulty;}
    public void setDifficulty(Difficulty difficulty) {this.difficulty = difficulty;}

    public String getImageURL() {return imageURL;}
    public void setImageURL(String imageURL) {this.imageURL = imageURL;}

    public String getPublicID() {return publicID;}
    public void setPublicID(String publicID) {this.publicID = publicID;}

    public MultipartFile getImage() {return image;}
    public void setImage(MultipartFile image) {this.image = image;}
}
