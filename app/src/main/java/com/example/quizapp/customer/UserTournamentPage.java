package com.example.quizapp.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quizapp.R;
import com.example.quizapp.adapters.TournamentListAdapter;
import com.example.quizapp.contoller.OnSuccessListner;
import com.example.quizapp.contoller.QuizConroller;
import com.example.quizapp.databinding.ActivityCustomerTornamentPageBinding;
import com.example.quizapp.databinding.ActivityTornamentPageBinding;
import com.example.quizapp.tournament.CreateNewTournament;
import com.example.quizapp.utils.SharedPrefsHelper;

public class UserTournamentPage extends AppCompatActivity {





    private ActivityCustomerTornamentPageBinding binding;

    private QuizConroller quizConroller;

    TournamentListAdapter adapter;

    SharedPrefsHelper sharedPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding  = ActivityCustomerTornamentPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        quizConroller = new QuizConroller();

        sharedPrefsHelper = new SharedPrefsHelper(this);


        adapter = new TournamentListAdapter(this,quizConroller.getTournamentList(), false);
        quizConroller.initAdapter(adapter);

        setRv(adapter);

        showProgree(true);

        loadData();



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