package com.backend.projectbackend.dto.training;

import java.util.Date;
import java.util.List;

public class GetTrainingSessionById {
    private List<GetTrainingSessionMarksByIdDTO> marks;
    private Date trainingDate;
    private String routineId;
    private String sessionId;

    public GetTrainingSessionById() {}

    public GetTrainingSessionById(List<GetTrainingSessionMarksByIdDTO> marks, Date trainingDate, String routineId, String sessionId) {
        this.marks = marks;
        this.trainingDate = trainingDate;
        this.routineId = routineId;
        this.sessionId = sessionId;
    }

    public List<GetTrainingSessionMarksByIdDTO> getMarks() {return marks;}
    public void setMarks(List<GetTrainingSessionMarksByIdDTO> marks) {this.marks = marks;}

    public Date getTrainingDate() {return trainingDate;}
    public void setTrainingDate(Date trainingDate) {this.trainingDate = trainingDate;}

    public String getRoutineId() {return routineId;}
    public void setRoutineId(String routineId) {this.routineId = routineId;}

    public String getSessionId() {return sessionId;}
    public void setSessionId(String sessionId) {this.sessionId = sessionId;}
}
