package com.example.quizapp.models;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TournamentModel {

    String id;
    String title;
    String startDate;
    String endDate;
    String categoryName;
    int categoryId;

    String difficulty;

    String type;


    public TournamentModel() {
    }

    public TournamentModel(String id, String title, String startDate, String endDate, String categoryName, int categoryId, String difficulty, String type) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.difficulty = difficulty;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String toJsonString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getTag(Date currentDate) throws ParseException {
         SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
        Date _startDate = DATE_FORMAT.parse(this.startDate);
        Date _endDate = DATE_FORMAT.parse(this.endDate);
        if (_startDate.before(currentDate) && _endDate.after(currentDate)) {
            return "Current"; // User can participate
        } else if (_endDate.before(currentDate)) {
            return "Old";
        } else {
            return "Upcoming";
        }
    }

}
