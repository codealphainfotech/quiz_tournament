package com.example.quizapp.tournament;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quizapp.adapters.UserListWithScoreAdapter;
import com.example.quizapp.contoller.OnFailed;
import com.example.quizapp.contoller.OnGetLikeDislikeListListener;
import com.example.quizapp.contoller.OnGetPlayedQuizModelsListener;
import com.example.quizapp.contoller.OnSuccessListner;
import com.example.quizapp.contoller.QuizConroller;
import com.example.quizapp.databinding.ActivityQuestionPageBinding;
import com.example.quizapp.models.QuizLikeModel;
import com.example.quizapp.models.QuizPlayedModel;
import com.example.quizapp.models.TournamentModel;
import com.example.quizapp.utils.ReusableAlertDialog;
import com.example.quizapp.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class TournamentDetailsPage extends AppCompatActivity {

    private String TAG = "TournamentDetailsPage";

    ImageView backIcon;

    private ActivityQuestionPageBinding binding;
    private QuizConroller quizConroller;

    private int tournamentIndex = 0;
    private String tournamentID = "";
    private TournamentModel tournamentModel;

    private UserListWithScoreAdapter adapter;

    ArrayList<QuizPlayedModel> quizPlayedModelsList = new ArrayList<>();

    private QuizLikeModel quizLikeModel;

    private  int totalLikeCount = 0;
    private  int totalDislikeCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityQuestionPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        quizConroller = new QuizConroller();

        Intent i = getIntent();
        if (i.getExtras() != null) {
            tournamentIndex = i.getIntExtra("index", 0);
            tournamentID = i.getStringExtra("id");
            tournamentModel = quizConroller.decodeTournamentFromJson(i.getStringExtra("model"));

            if (tournamentModel != null) {
                setData();
            }
        }


        binding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.ivBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReusableAlertDialog.showConfirmationDialog(TournamentDetailsPage.this, "Delete Tournament?", "This will delete all the info related with this Tournament. This action can not be undone.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTournament();
                    }
                });
            }
        });

        ActivityResultLauncher<Intent> editLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == 123) {//update ui
                    tournamentModel = quizConroller.decodeTournamentFromJson(result.getData().getStringExtra("model"));
                    Log.e(TAG, "onActivityResult: Reset new data");
                    setData();
                }
            }
        });

        binding.ivBtnEdit.setOnClickListener(v -> {
            Intent editIntent = new Intent(TournamentDetailsPage.this, CreateNewTournament.class);
            editIntent.putExtra("model", tournamentModel.toJsonString());
            editLauncher.launch(editIntent);
        });


        //get data
        setRv();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserPlayedData();
        getTotalLikeCount();
    }


    void getTotalLikeCount(){
        quizConroller.getQuizLikeByTournamentId(tournamentModel.getId(), new OnGetLikeDislikeListListener() {
            @Override
            public void onGetLikeDislikeList(List<QuizLikeModel> likeDislikeList) {
                Log.e(TAG, "onGetLikeDislikeList: "+ likeDislikeList.toString() );
                if (!likeDislikeList.isEmpty()){
                    for (QuizLikeModel q: likeDislikeList) {
                        if (q.getType().equals("like")){
                            totalLikeCount += 1;
                        }else if (q.getType().equals("dislike")){
                            totalDislikeCount += 1;
                        }
                    }

                    updateLikeUi();
                }
            }

            @Override
            public void onGetError(String message) {

            }
        });
    }


    void updateLikeUi(){
        binding.tvLikeCount.setText(String.format("%d", totalLikeCount));
        binding.tvDislikeCount.setText(String.format("%d", totalDislikeCount));
    }

    void setRv() {
        adapter = new UserListWithScoreAdapter(quizPlayedModelsList, this);
        binding.rvUsersList.setLayoutManager(new LinearLayoutManager(TournamentDetailsPage.this));
        binding.rvUsersList.setAdapter(adapter);
    }


    public void getUserPlayedData() {
        showProgress(true);
        quizConroller.getPlayedQuizModelsByTournamentId(tournamentModel.getId(), new OnGetPlayedQuizModelsListener() {
            @Override
            public void onGetPlayedQuizModels(List<QuizPlayedModel> playedQuizModels) {
                quizPlayedModelsList = (ArrayList<QuizPlayedModel>) playedQuizModels;
                showProgress(false);
                setRv();
            }

            @Override
            public void onGetError(String message) {
                showProgress(false);
                setData();
            }
        });
    }


    void showProgress(boolean show) {
        if (show) {
            binding.rvUsersList.setVisibility(View.GONE);
            binding.progressCircularView.setVisibility(View.VISIBLE);
        } else {
            binding.rvUsersList.setVisibility(View.VISIBLE);
            binding.progressCircularView.setVisibility(View.GONE);
        }
    }

    void setData() {
        binding.tvTitle.setText(tournamentModel.getTitle());
        binding.tvCategory.setText(tournamentModel.getCategoryName());
        binding.tvstartDate.setText(tournamentModel.getStartDate());
        binding.tvEndDate.setText(tournamentModel.getEndDate());
        binding.tvDifficulty.setText(tournamentModel.getDifficulty().toUpperCase());
    }

    void deleteTournament() {
        quizConroller.deleteTournament(tournamentModel.getId(), new OnSuccessListner() {
            @Override
            public void onSuccess() {
                ToastUtils.successToast(TournamentDetailsPage.this, "Tournament Deleted!");
            }
        }, new OnFailed() {
            @Override
            public void onFailed() {

            }
        });
    }
}