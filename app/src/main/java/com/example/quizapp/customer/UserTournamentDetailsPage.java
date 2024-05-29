package com.example.quizapp.customer;

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

import com.example.quizapp.R;
import com.example.quizapp.contoller.OnFailed;
import com.example.quizapp.contoller.OnSuccessListner;
import com.example.quizapp.contoller.QuizConroller;
import com.example.quizapp.databinding.ActivityQuestionPageBinding;
import com.example.quizapp.databinding.ActivityUserQuestionPageBinding;
import com.example.quizapp.models.TournamentModel;
import com.example.quizapp.tournament.CreateNewTournament;
import com.example.quizapp.utils.ReusableAlertDialog;
import com.example.quizapp.utils.ToastUtils;

public class UserTournamentDetailsPage extends AppCompatActivity {

    private String TAG = "TournamentDetailsPage";

    ImageView backIcon;

    private ActivityUserQuestionPageBinding binding;
    private QuizConroller quizConroller;

    private  int tournamentIndex = 0;
    private  String tournamentID = "";
    private  TournamentModel tournamentModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding  =  ActivityUserQuestionPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        quizConroller = new QuizConroller();


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

    }

    void setData(){
        binding.tvTitle.setText(tournamentModel.getTitle());
        binding.tvCategory.setText(tournamentModel.getCategoryName());
        binding.tvstartDate.setText(tournamentModel.getStartDate());
        binding.tvEndDate.setText(tournamentModel.getEndDate());
        binding.tvDifficulty.setText(tournamentModel.getDifficulty().toUpperCase());
    }
}