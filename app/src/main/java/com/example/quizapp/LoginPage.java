package com.example.quizapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.HomepageCards.TournamentPage;
import com.example.quizapp.contoller.OnGetUserListner;
import com.example.quizapp.contoller.UserController;
import com.example.quizapp.customer.UserTournamentPage;
import com.example.quizapp.databinding.ActivityLoginPageBinding;
import com.example.quizapp.models.UserModel;
import com.example.quizapp.utils.SharedPrefsHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginPage extends AppCompatActivity {

    Button BtnSignup ,BtnLogIn;

    private static final String TAG = "SignUpPage";
    private FirebaseAuth mAuth;

    private ActivityLoginPageBinding binding;

    private UserController userController;

    private SharedPrefsHelper sharedPrefsHelper;





    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        binding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        userController = new UserController();

        sharedPrefsHelper = new SharedPrefsHelper(this);


        BtnSignup = findViewById(R.id.btn_signup);
        BtnLogIn = findViewById(R.id.btn_login);

        BtnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etEmail.getText().toString().isEmpty()){
                    errorMsg("Enter Email");
                }else if (binding.etPassword.getText().toString().isEmpty()){
                    errorMsg("Enter Passowrd");
                }else{
                    signIn(binding.etEmail.getText().toString().trim(),binding.etPassword.getText().toString().trim() );

                }
            }
        });

        BtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(LoginPage.this, SignUpPage.class);
                LoginPage.this.startActivity(mainIntent);

            }
        });


    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();

                            userController.getUserById(currentUser.getUid(), new OnGetUserListner() {
                                @Override
                                public void onGetUserSuccess(UserModel user) {
                                    Log.e(TAG, "onGetUserSuccess: role :" + user.getRole() );
                                    sharedPrefsHelper.saveUserRole(user.getRole());
                                    Intent mainIntent;
                                    if (Objects.equals(user.getRole(), "admin")){
                                        mainIntent = new Intent(LoginPage.this, HomepageList.class);
                                    }else{
                                        mainIntent = new Intent(LoginPage.this, UserTournamentPage.class);
                                    }
                                    LoginPage.this.startActivity(mainIntent);
                                    finish();
                                }

                                @Override
                                public void onGetUserError(String message) {

                                }
                            });




                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginPage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        // [END sign_in_with_email]
    }

    void  errorMsg(String msg){
        Toast.makeText(LoginPage.this, msg,
                Toast.LENGTH_SHORT).show();
    }
}