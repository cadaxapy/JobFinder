package com.example.jobfounder;

import java.util.List;

public class JobModel {
    private String jobName, land, type, imgURL;
    private List<String> professions;

    public String getJobName() {
        return jobName;
    }

    public String getLand() {
        return land;
    }

    public String getType() {
        return type;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public void setProfessions(List<String> professions) {
        this.professions = professions;
    }

    public String getImgURL() {
        return imgURL;
    }

    public List<String> getProfessions() {
        return professions;
    }
}
