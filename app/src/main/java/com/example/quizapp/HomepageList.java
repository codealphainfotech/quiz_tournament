package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.Admin.QuestionPage;
import com.example.quizapp.HomepageCards.CatagoryPage;
import com.example.quizapp.HomepageCards.TornamentPage;
import com.example.quizapp.HomepageCards.UsersPage;

public class HomepageList extends AppCompatActivity {

    CardView cardCatagory,cardTurnaments,cardUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage_list);

        cardCatagory = findViewById(R.id.Catagorycard);
        cardTurnaments = findViewById(R.id.TurnamentsCard);
        cardUsers = findViewById(R.id.UsersCard);

        cardCatagory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageList.this, CatagoryPage.class);
                startActivity(intent);
            }
        });
        cardTurnaments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageList.this, TornamentPage.class);
                startActivity(intent);
            }
        });

        cardUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageList.this, UsersPage.class);
                startActivity(intent);
            }
        });


    }
}