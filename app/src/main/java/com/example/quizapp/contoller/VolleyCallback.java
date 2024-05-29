package com.example.quizapp.contoller;

import com.example.quizapp.models.ApiResponse;

import java.util.List;

public interface VolleyCallback {
    void onSuccess(List<ApiResponse.Result> results);
    void onError(String message);
}
