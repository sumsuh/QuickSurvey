package com.example.quicksurvey;

public class Survey {

    String name;
    int surveyid;
    String deadline;

    public Survey(String name, int surveyid, String deadline) {
        this.name = name;
        this.surveyid = surveyid;
        this.deadline = deadline;
    }

    public String getName() {
        return name;
    }



    public int getSurveyid() {
        return surveyid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurveyid(int surveyid) {
        this.surveyid = surveyid;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDeadline() {
        return deadline;
    }
}
