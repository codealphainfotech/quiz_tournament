package com.example.quizapp.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quizapp.LoginPage;
import com.example.quizapp.R;
import com.example.quizapp.adapters.TournamentListAdapter;
import com.example.quizapp.contoller.OnGetPlayedQuizModelsListener;
import com.example.quizapp.contoller.OnSuccessListner;
import com.example.quizapp.contoller.QuizConroller;
import com.example.quizapp.databinding.ActivityCustomerTornamentPageBinding;
import com.example.quizapp.databinding.ActivityTornamentPageBinding;
import com.example.quizapp.models.QuizPlayedModel;
import com.example.quizapp.models.TournamentModel;
import com.example.quizapp.tournament.CreateNewTournament;
import com.example.quizapp.utils.AppString;
import com.example.quizapp.utils.SharedPrefsHelper;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserTournamentPage extends AppCompatActivity {


    private ActivityCustomerTornamentPageBinding binding;

    private QuizConroller quizConroller;

    TournamentListAdapter adapter;

    SharedPrefsHelper sharedPrefsHelper;

    private FirebaseAuth mAuth;

    int defaultSelectedTab = 0;
    String defTag = AppString.strOngoing;
    ArrayList<TournamentModel> participatedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityCustomerTornamentPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        quizConroller = new QuizConroller();

        sharedPrefsHelper = new SharedPrefsHelper(this);

        mAuth = FirebaseAuth.getInstance();


        adapter = new TournamentListAdapter(this, quizConroller.getTournamentList(), false);
        quizConroller.initAdapter(adapter);

        setRv(adapter);

        showProgree(true);

        loadData();
        getParticipatedData();


        binding.logoutIcon.setOnClickListener(v -> {
            mAuth.signOut();
            sharedPrefsHelper.clearOnLogout();
            Intent intent = new Intent(this, LoginPage.class);
            startActivity(intent);
            finish();
        });


        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Ongoing"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Upcoming"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Old"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Participated"));

        binding.tabLayout.getTabAt(defaultSelectedTab).select();
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Handle tab selection if needed
                int pos = tab.getPosition();
                defaultSelectedTab = pos;

                switch (pos) {

                    case 0:
                        defTag = AppString.strOngoing;
                        break;
                    case 1:

                        defTag = "Upcoming";
                        break;
                    case 2:

                        defTag = "Old";
                        break;

                    case 3:
                        defTag = AppString.strParticipated;
                        break;
                    default:
                        defTag = "Ongoing";
                        break;
                }

                if (defTag.equals(AppString.strParticipated)){
                    adapter.updateList(participatedList);

                }else {
                    filterListByTag(defTag);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle tab reselection if needed
            }
        });


    }

    void filterListByTag(String filterTag) {
        List<TournamentModel> filteredList = new ArrayList<>();

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
    protected void onResume() {
        super.onResume();
        loadData();
    }

    void loadData() {
        quizConroller.getTournamentFromDb(new OnSuccessListner() {
            @Override
            public void onSuccess() {
                showProgree(false);
                if (defTag.equals(AppString.strParticipated)){
                    getParticipatedData();
                }else {
                    filterListByTag(defTag);
                }
                binding.tabLayout.getTabAt(defaultSelectedTab).select();
            }
        });
    }

    void getParticipatedData(){
        String userId = sharedPrefsHelper.getUserModelFromSharedPref().getUserID();

        if (!participatedList.isEmpty()){
            participatedList.clear();
        }
        quizConroller.getQuizPlayedModelsByUserId(userId, new OnGetPlayedQuizModelsListener() {
            @Override
            public void onGetPlayedQuizModels(List<QuizPlayedModel> playedQuizModels) {
                for (QuizPlayedModel q: playedQuizModels
                ) {
                    participatedList.add(q.getTournamentModel());
                }
            }

            @Override
            public void onGetError(String message) {

            }
        });
    }

    void setRv(TournamentListAdapter adapter) {
        binding.rvTournamentList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTournamentList.setAdapter(adapter);
    }

    void showProgree(boolean show) {
        if (show) {
            binding.rvTournamentList.setVisibility(View.GONE);
            binding.progressCircularView.setVisibility(View.VISIBLE);
        } else {
            binding.rvTournamentList.setVisibility(View.VISIBLE);
            binding.progressCircularView.setVisibility(View.GONE);
        }
    }
}