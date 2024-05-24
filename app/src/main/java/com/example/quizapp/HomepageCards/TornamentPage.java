package com.example.quizapp.HomepageCards;

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
import com.example.quizapp.CreateNewTurnamenr;
import com.example.quizapp.HomepageList;
import com.example.quizapp.R;

public class TornamentPage extends AppCompatActivity {
    Button BtnCreatNewTurnament;
    ImageView BackButton;

    CardView card_to_details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tornament_page);
        BtnCreatNewTurnament = findViewById(R.id.Btn_CreateTurnament);

        BackButton = findViewById(R.id.backIcon);

        card_to_details = findViewById(R.id.information_card);

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        BtnCreatNewTurnament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TornamentPage.this, CreateNewTurnamenr.class);
                startActivity(intent);



            }
        });

        card_to_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TornamentPage.this, QuestionPage.class);
                startActivity(intent);


            }
        });



    }
}