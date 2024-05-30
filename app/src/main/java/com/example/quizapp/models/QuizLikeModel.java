package com.example.quizapp.models;

import java.util.Date;

public class QuizLikeModel {
    String id;
    String tournamentId;
    String userId;

    String type;

    Date likedOn;

    public QuizLikeModel(String id, String tournamentId, String userId, String type, Date likedOn) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.userId = userId;
        this.type = type;
        this.likedOn = likedOn;
    }

    public QuizLikeModel() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getLikedOn() {
        return likedOn;
    }

    public void setLikedOn(Date likedOn) {
        this.likedOn = likedOn;
    }
}
