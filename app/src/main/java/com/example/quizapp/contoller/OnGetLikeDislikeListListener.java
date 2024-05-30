package com.example.quizapp.contoller;

import com.example.quizapp.models.QuizLikeModel;

import java.util.List;

public interface OnGetLikeDislikeListListener {
    void onGetLikeDislikeList(List<QuizLikeModel> likeDislikeList);
    void onGetError(String message);
}