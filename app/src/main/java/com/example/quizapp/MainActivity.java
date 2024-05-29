package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.HomepageCards.TournamentPage;
import com.example.quizapp.contoller.OnGetUserListner;
import com.example.quizapp.contoller.UserController;
import com.example.quizapp.customer.UserTournamentPage;
import com.example.quizapp.models.UserModel;
import com.example.quizapp.utils.SharedPrefsHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000;

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;

    private UserController userController;
    private SharedPrefsHelper sharedPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        userController = new UserController();
        sharedPrefsHelper = new SharedPrefsHelper(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              checklogin();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    void checklogin(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //  reload();

            userController.getUserById(currentUser.getUid(), new OnGetUserListner() {
                @Override
                public void onGetUserSuccess(UserModel user) {
                    Log.e(TAG, "onGetUserSuccess: role :" + user.getRole() );

                    sharedPrefsHelper.saveUserRole(user.getRole());
                    sharedPrefsHelper.saveUserModelToSharedPref(user);

                    Intent mainIntent;
                    if (Objects.equals(user.getRole(), "admin")){
                        mainIntent = new Intent(MainActivity.this, HomepageList.class);
                    }else{//users
                        mainIntent = new Intent(MainActivity.this, UserTournamentPage.class);
                    }
                    MainActivity.this.startActivity(mainIntent);
                    finish();
                }

                @Override
                public void onGetUserError(String message) {

                }
            });

        }else {
            // Start the main activity
            Intent mainIntent = new Intent(MainActivity.this, LoginPage.class);
            MainActivity.this.startActivity(mainIntent);
            MainActivity.this.finish();
        }
    }
}