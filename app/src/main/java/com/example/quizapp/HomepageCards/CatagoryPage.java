package com.example.quizapp.HomepageCards;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.AddCatagoryPage;
import com.example.quizapp.R;

public class CatagoryPage extends AppCompatActivity {

    Button btnCtagories;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catagory_page);

        btnCtagories = findViewById(R.id.btnCtagories);
        btnBack = findViewById(R.id.backIcon);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCtagories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CatagoryPage.this, AddCatagoryPage.class);
                startActivity(intent);
            }
        });
    }
}