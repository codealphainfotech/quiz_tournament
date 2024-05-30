package com.example.quizapp.contoller;

public interface OnSendPasswordResetListener {
    void onPasswordResetSent();
    void onSendPasswordResetError(String message);
}
