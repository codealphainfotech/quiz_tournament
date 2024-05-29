package com.example.quizapp.customer;

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

import com.example.quizapp.R;
import com.example.quizapp.contoller.OnGetQuizPlayedModelListener;
import com.example.quizapp.contoller.QuizConroller;
import com.example.quizapp.databinding.ActivityUserQuestionPageBinding;
import com.example.quizapp.models.QuizPlayedModel;
import com.example.quizapp.models.TournamentModel;
import com.example.quizapp.models.UserModel;
import com.example.quizapp.utils.HelperUtils;
import com.example.quizapp.utils.SharedPrefsHelper;

public class UserTournamentDetailsPage extends AppCompatActivity {

    private String TAG = "TournamentDetailsPage";

    ImageView backIcon;

    private ActivityUserQuestionPageBinding binding;
    private QuizConroller quizConroller;

    private  int tournamentIndex = 0;
    private  String tournamentID = "";
    private  TournamentModel tournamentModel;

    private UserModel userModel;

    private SharedPrefsHelper prefsHelper;

    boolean isplayed  = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding  =  ActivityUserQuestionPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        quizConroller = new QuizConroller();
        prefsHelper = new SharedPrefsHelper(this);

        userModel = prefsHelper.getUserModelFromSharedPref();


        Intent i = getIntent();
        if (i.getExtras() != null){
            tournamentIndex = i.getIntExtra("index", 0);
            tournamentID = i.getStringExtra("id");
            Log.e(TAG, "onCreate: index :" + tournamentIndex );
            Log.e(TAG, "onCreate: id :" + tournamentID );
            Log.e(TAG, "onCreate: " + i.getStringExtra("model"));

            tournamentModel = quizConroller.decodeTournamentFromJson(i.getStringExtra("model"));

            if (tournamentModel != null){
                setData();
            }
        }


        backIcon = findViewById(R.id.backIcon);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserTournamentDetailsPage.this, UserQuizPage.class);
                i.putExtra("id", tournamentModel.getId());
                i.putExtra("model", tournamentModel.toJsonString());
                startActivity(i);
            }
        });


        ActivityResultLauncher<Intent> editLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == 123){//update ui
                    tournamentModel = quizConroller.decodeTournamentFromJson(result.getData().getStringExtra("model"));
                    Log.e(TAG, "onActivityResult: Reset new data" );
                    setData();
                }
            }
        });

        getPlayedData();


    }

    @Override
    protected void onResume() {
        super.onResume();
        getPlayedData();
    }

    void getPlayedData(){
        showProgress(true);
        try {
            quizConroller.getQuizPlayedModelByUserIdAndTournamentId(userModel.getUserID(), tournamentModel.getId(), new OnGetQuizPlayedModelListener() {
                @Override
                public void onGetQuizPlayedModel(QuizPlayedModel quizPlayedModel) {
                    showProgress(false);
                    if (quizPlayedModel != null){

                        String date = HelperUtils.convertDateToStr(quizPlayedModel.getPlayedDate());
                        binding.tvQuizDate.setText(date);
                        binding.tvQuizScore.setText("" + quizPlayedModel.getScore());
                        showStartButton(false);
                    }else{
                        showStartButton(true);
                    }
                }

                @Override
                public void onGetError(String message) {
                    showProgress(false);
                    showStartButton(true);
                    Log.e(TAG, "onGetError: " + message);
                }
            });
        }catch (Exception e){
            Log.e(TAG, "getPlayedData: " + e.getMessage() );
        }

    }

    void setData(){
        binding.tvTitle.setText(tournamentModel.getTitle());
        binding.tvCategory.setText(tournamentModel.getCategoryName());
        binding.tvstartDate.setText(tournamentModel.getStartDate());
        binding.tvEndDate.setText(tournamentModel.getEndDate());
        binding.tvDifficulty.setText(tournamentModel.getDifficulty().toUpperCase());
    }

    void showProgress(boolean show){
        if (show){
            binding.lvDetailsView.setVisibility(View.GONE);
            binding.progressCircularView.setVisibility(View.VISIBLE);
        }else {
            binding.lvDetailsView.setVisibility(View.VISIBLE);
            binding.progressCircularView.setVisibility(View.GONE);
        }
    }

    void showStartButton(boolean isShow){
        if (isShow){
            binding.lvPlayedQuizView.setVisibility(View.GONE);
            binding.btnStartQuiz.setVisibility(View.VISIBLE);

        }else {
            binding.lvPlayedQuizView.setVisibility(View.VISIBLE);
            binding.btnStartQuiz.setVisibility(View.GONE);
        }
    }
}