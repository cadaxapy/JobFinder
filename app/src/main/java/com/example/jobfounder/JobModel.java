package com.example.jobfounder;

import java.util.Date;
import java.util.List;

public class JobModel {
    private String jobName;
    private String land;
    private String type;
    private String imgURL;
    private String subjectArea;
    private String email;
    private String city;
    private String address;
    private String plz;
    private boolean isImportant;

    public boolean getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(boolean isImportant) {
        this.isImportant = isImportant;
    }

    public boolean getIsOptional() {
        return isOptional;
    }

    public void setIsOptional(boolean isOptional) {
        this.isOptional = isOptional;
    }

    private boolean isOptional;

    public String getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(String favoriteId) {
        this.favoriteId = favoriteId;
    }

    private String favoriteId;
    private Date activeBy;

    public void setSubjectArea(String subjectArea) {
        this.subjectArea = subjectArea;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    private List<String> professions;
    public String getSubjectArea() {
        return subjectArea;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getPlz() {
        return plz;
    }

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

    public Date getActiveBy() {
        return activeBy;
    }

    public void setActiveBy(Date activeBy) {
        this.activeBy = activeBy;
    }
}
