package com.example.quizapp.HomepageCards;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quizapp.adapters.TournamentListAdapter;
import com.example.quizapp.contoller.OnSuccessListner;
import com.example.quizapp.contoller.QuizConroller;
import com.example.quizapp.databinding.ActivityTornamentPageBinding;
import com.example.quizapp.tournament.CreateNewTournament;
import com.example.quizapp.R;

public class TournamentPage extends AppCompatActivity {

    ImageView BackButton;

    CardView card_to_details;


    private ActivityTornamentPageBinding binding;

    private QuizConroller quizConroller;

    TournamentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding  = ActivityTornamentPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        quizConroller = new QuizConroller();

        adapter = new TournamentListAdapter(this,quizConroller.getTournamentList(), true);
        quizConroller.initAdapter(adapter);

        setRv(adapter);

        showProgree(true);

        loadData();
        BackButton = findViewById(R.id.backIcon);

        card_to_details = findViewById(R.id.information_card);

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.BtnCreateTurnament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TournamentPage.this, CreateNewTournament.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    void loadData(){
        quizConroller.getTournamentFromDb(new OnSuccessListner() {
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();
                showProgree(false);
            }
        });
    }

  void  setRv(TournamentListAdapter adapter){
        binding.rvTournamentList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTournamentList.setAdapter(adapter);
    }

    void showProgree(boolean show){
        if (show){
            binding.rvTournamentList.setVisibility(View.GONE);
            binding.progressCircularView.setVisibility(View.VISIBLE);
        }else {
            binding.rvTournamentList.setVisibility(View.VISIBLE);
            binding.progressCircularView.setVisibility(View.GONE);
        }
    }
}