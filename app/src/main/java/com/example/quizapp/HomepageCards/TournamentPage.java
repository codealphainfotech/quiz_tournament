package com.example.quizapp.HomepageCards;

import static java.util.Locale.filter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quizapp.adapters.TournamentListAdapter;
import com.example.quizapp.contoller.OnSuccessListner;
import com.example.quizapp.contoller.QuizConroller;
import com.example.quizapp.databinding.ActivityTornamentPageBinding;
import com.example.quizapp.models.TournamentModel;
import com.example.quizapp.tournament.CreateNewTournament;
import com.example.quizapp.R;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.material.tabs.TabLayout;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TournamentPage extends AppCompatActivity {

    ImageView BackButton;

    CardView card_to_details;
    TabLayout tabLayout;


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
        tabLayout = findViewById(R.id.tabLayout);

        card_to_details = findViewById(R.id.information_card);

        tabLayout.addTab(tabLayout.newTab().setText("Current"));
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"));
        tabLayout.addTab(tabLayout.newTab().setText("Old"));

// Optional: Set a default selected tab
        tabLayout.getTabAt(0).select();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Handle tab selection if needed
                List<TournamentModel> filteredList = new ArrayList<>();
                int pos = tab.getPosition();
                String filterTag = "";

                switch (pos){

                    case 0:
                        filterTag = "Current";
                        break;
                    case 1:

                        filterTag = "Upcoming";
                        break;
                    case 2:

                        filterTag = "Old";
                        break;
                    default:
                        filterTag = "Current";
                        break;
                }

                for (TournamentModel tournament : quizConroller.getTournamentList()) {
                    // Filter the tournament list based on the selected tab
                    try {
                        if (tournament.getTag(new Date()).equals(filterTag)) {
                            filteredList.add(tournament);
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }

                // Update RecyclerView with the filtered list
                adapter.updateList(filteredList);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle tab unselection if needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle tab reselection if needed
            }
        });


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