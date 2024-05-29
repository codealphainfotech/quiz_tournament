package com.example.quizapp.models;

import java.util.Date;

public class QuizPlayedModel {

    String id;

    //quiz
    TournamentModel tournamentModel;

    UserModel userModel;

    Date playedDate;

    int score;

    public QuizPlayedModel(String id, TournamentModel tournamentModel, UserModel userModel, Date playedDate, int score) {
        this.id = id;
        this.tournamentModel = tournamentModel;
        this.userModel = userModel;
        this.playedDate = playedDate;
        this.score = score;
    }

    public QuizPlayedModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TournamentModel getTournamentModel() {
        return tournamentModel;
    }

    public void setTournamentModel(TournamentModel tournamentModel) {
        this.tournamentModel = tournamentModel;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public Date getPlayedDate() {
        return playedDate;
    }

    public void setPlayedDate(Date playedDate) {
        this.playedDate = playedDate;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
