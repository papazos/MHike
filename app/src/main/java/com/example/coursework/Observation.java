package com.example.coursework;

public class Observation {
    private int id;
    private Integer hikeId;
    private String observation;
    private String observationTime;
    private String observationComments;

    public Observation(int id, Integer hikeId, String observation, String observationTime, String observationComments) {
        this.id = id;
        this.hikeId = hikeId;
        this.observation = observation;
        this.observationTime = observationTime;
        this.observationComments = observationComments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getHikeId() {
        return hikeId;
    }

    public void setHikeId(Integer hikeId) {
        this.hikeId = hikeId;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getObservationTime() {
        return observationTime;
    }

    public void setObservationTime(String observationTime) {
        this.observationTime = observationTime;
    }

    public String getObservationComments() {
        return observationComments;
    }

    public void setObservationComments(String observationComments) {
        this.observationComments = observationComments;
    }
}
