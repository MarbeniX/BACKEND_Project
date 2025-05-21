package com.backend.projectbackend.dto.training;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SessionMarksDTO {
    @NotBlank
    private String exerciseId;
    @NotNull
    private Long timeToComplete;
    @NotNull
    private Integer setNumber;
    @NotNull
    private Integer reps;
    @NotBlank
    private String trainingSessionId;

    public SessionMarksDTO() {}

    public SessionMarksDTO(String exerciseId, Long timeToComplete, Integer setNumber, Integer reps, String trainingSessionId) {
        this.exerciseId = exerciseId;
        this.timeToComplete = timeToComplete;
        this.setNumber = setNumber;
        this.reps = reps;
        this.trainingSessionId = trainingSessionId;
    }

    public String getExerciseId() { return exerciseId; }
    public Long getTimeToComplete() { return timeToComplete; }
    public Integer getSetNumber() { return setNumber; }
    public Integer getReps() { return reps; }
    public String getTrainingSessionId() { return trainingSessionId; }
}
