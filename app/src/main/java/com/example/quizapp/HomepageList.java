package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.quizapp.HomepageCards.TournamentPage;
import com.example.quizapp.HomepageCards.UsersPage;
import com.example.quizapp.databinding.ActivityHomepageListBinding;
import com.google.firebase.auth.FirebaseAuth;

public class HomepageList extends AppCompatActivity {

    CardView cardTurnaments,cardUsers;


    private ActivityHomepageListBinding binding;

    private static final String TAG = "HomepageList";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityHomepageListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();

        cardTurnaments = findViewById(R.id.TurnamentsCard);
        cardUsers = findViewById(R.id.UsersCard);

        cardTurnaments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomepageList.this, TournamentPage.class);
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

        binding.logoutIcon.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(this, LoginPage.class);
            startActivity(intent);
            finish();
        });


    }
}