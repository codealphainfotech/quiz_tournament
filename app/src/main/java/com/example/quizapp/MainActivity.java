package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000;

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

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
            Intent mainIntent = new Intent(MainActivity.this, HomepageList.class);
            MainActivity.this.startActivity(mainIntent);
            MainActivity.this.finish();
        }else {
            // Start the main activity
            Intent mainIntent = new Intent(MainActivity.this, LoginPage.class);
            MainActivity.this.startActivity(mainIntent);
            MainActivity.this.finish();
        }
    }
}