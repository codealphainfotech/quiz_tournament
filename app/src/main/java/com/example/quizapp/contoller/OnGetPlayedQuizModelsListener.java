package com.example.quizapp.contoller;

import com.example.quizapp.models.QuizPlayedModel;
import com.example.quizapp.models.UserModel;

import java.util.HashMap;
import java.util.List;

public interface OnGetPlayedQuizModelsListener {
    void onGetPlayedQuizModels(List<QuizPlayedModel> playedQuizModels);
    void onGetError(String message);
}