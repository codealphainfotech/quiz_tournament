package com.example.quizapp.contoller;

import com.example.quizapp.models.UserModel;

public interface OnGetUserListner {
    void onGetUserSuccess(UserModel user);
    void onGetUserError(String message);
}
