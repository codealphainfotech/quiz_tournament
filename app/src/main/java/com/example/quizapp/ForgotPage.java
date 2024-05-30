package com.example.quizapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizapp.contoller.OnGetUserListner;
import com.example.quizapp.contoller.OnSendPasswordResetListener;
import com.example.quizapp.contoller.UserController;
import com.example.quizapp.customer.UserTournamentPage;
import com.example.quizapp.databinding.ActivityForgotPageBinding;
import com.example.quizapp.databinding.ActivityLoginPageBinding;
import com.example.quizapp.models.UserModel;
import com.example.quizapp.tournament.TournamentDetailsPage;
import com.example.quizapp.utils.ReusableAlertDialog;
import com.example.quizapp.utils.SharedPrefsHelper;
import com.example.quizapp.utils.ToastUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ForgotPage extends AppCompatActivity {

    Button BtnSignup, BtnLogIn;

    private static final String TAG = "SignUpPage";
    private FirebaseAuth auth;

    private ActivityForgotPageBinding binding;

    private UserController userController;

    private SharedPrefsHelper sharedPrefsHelper;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        binding = ActivityForgotPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        userController = new UserController();

        sharedPrefsHelper = new SharedPrefsHelper(this);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etEmail.getText().toString().isEmpty()) {
                    errorMsg("Enter Email");
                } else {
                    showProgress(true);
                    sendPasswordResetEmail(binding.etEmail.getText().toString().trim(), new OnSendPasswordResetListener() {
                        @Override
                        public void onPasswordResetSent() {
                            showProgress(false);
                            ReusableAlertDialog.showConfirmationDialog(ForgotPage.this, "Password Reset Link Sent!", "Check your Email, You Will receive Password Reset Link to Reset You Password and Try Login Again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                  finish();
                                }
                            });
                        }

                        @Override
                        public void onSendPasswordResetError(String message) {
                            ToastUtils.errorToast(ForgotPage.this, message.toString());
                            showProgress(false);
                        }
                    });
                }
            }
        });


    }

    void errorMsg(String msg) {
        Toast.makeText(ForgotPage.this, msg,
                Toast.LENGTH_SHORT).show();
    }

    void showProgress(boolean show) {
        if (show) {
            binding.btnLogin.setVisibility(View.GONE);
            binding.progressCircularView.setVisibility(View.VISIBLE);
        } else {
            binding.btnLogin.setVisibility(View.VISIBLE);
            binding.progressCircularView.setVisibility(View.GONE);
        }
    }

    public void sendPasswordResetEmail(String email, OnSendPasswordResetListener listener) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onPasswordResetSent();
                    } else {
                        listener.onSendPasswordResetError(task.getException().getMessage());
                    }
                });
    }
}