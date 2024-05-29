package com.example.quizapp.contoller;

import com.example.quizapp.models.QuizPlayedModel;

public interface OnGetQuizPlayedModelListener {
    void onGetQuizPlayedModel(QuizPlayedModel quizPlayedModel);
    void onGetError(String message);
}
