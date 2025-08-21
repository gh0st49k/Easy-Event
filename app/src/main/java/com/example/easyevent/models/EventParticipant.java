package com.example.easyevent.models;

public class EventParticipant {
    private String participantId;
    private String name;
    private String mobile;
    private String college;
    private String semester;

    // Default constructor required for Firebase
    public EventParticipant() {}

    public EventParticipant(String participantId, String name, String mobile, String college, String semester) {
        this.participantId = participantId;
        this.name = name;
        this.mobile = mobile;
        this.college = college;
        this.semester = semester;
    }

    // Getters and Setters
    public String getParticipantId() { return participantId; }
    public void setParticipantId(String participantId) { this.participantId = participantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
}
