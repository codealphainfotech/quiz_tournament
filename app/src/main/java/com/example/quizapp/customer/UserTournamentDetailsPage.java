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
import com.example.quizapp.contoller.OnAddLikeDislikeListener;
import com.example.quizapp.contoller.OnGetLikeDislikeListListener;
import com.example.quizapp.contoller.OnGetPlayedQuizModelsListener;
import com.example.quizapp.contoller.OnGetQuizPlayedModelListener;
import com.example.quizapp.contoller.QuizConroller;
import com.example.quizapp.databinding.ActivityUserQuestionPageBinding;
import com.example.quizapp.models.QuizLikeModel;
import com.example.quizapp.models.QuizPlayedModel;
import com.example.quizapp.models.TournamentModel;
import com.example.quizapp.models.UserModel;
import com.example.quizapp.utils.AppString;
import com.example.quizapp.utils.HelperUtils;
import com.example.quizapp.utils.ReusableAlertDialog;
import com.example.quizapp.utils.SharedPrefsHelper;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class UserTournamentDetailsPage extends AppCompatActivity {

    private String TAG = "TournamentDetailsPage";

    ImageView backIcon;

    private ActivityUserQuestionPageBinding binding;
    private QuizConroller quizConroller;

    private int tournamentIndex = 0;
    private String tournamentID = "";
    private TournamentModel tournamentModel;

    private UserModel userModel;

    private SharedPrefsHelper prefsHelper;


    private QuizLikeModel quizLikeModel;

    private int totalLikeCount = 0;
    private int totalDislikeCount = 0;

    private boolean isCurrent = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUserQuestionPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        quizConroller = new QuizConroller();
        prefsHelper = new SharedPrefsHelper(this);

        userModel = prefsHelper.getUserModelFromSharedPref();


        Intent i = getIntent();
        if (i.getExtras() != null) {
            tournamentIndex = i.getIntExtra("index", 0);
            tournamentID = i.getStringExtra("id");
            Log.e(TAG, "onCreate: index :" + tournamentIndex);
            Log.e(TAG, "onCreate: id :" + tournamentID);
            Log.e(TAG, "onCreate: " + i.getStringExtra("model"));

            tournamentModel = quizConroller.decodeTournamentFromJson(i.getStringExtra("model"));

            if (tournamentModel != null) {
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


                if (isCurrent){
                    Intent i = new Intent(UserTournamentDetailsPage.this, UserQuizPage.class);
                    i.putExtra("id", tournamentModel.getId());
                    i.putExtra("model", tournamentModel.toJsonString());
                    startActivity(i);
                }else {
                    String tag = binding.tvStatusLable.getText().toString();
                    ReusableAlertDialog.showConfirmationDialog(UserTournamentDetailsPage.this, "This event is " + tag + ".", "You Can Only Join or start Current Tournaments", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }

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

        getPlayedData();


        binding.ivBtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (isCurrent){
//                    quizConroller.addLikeDislikeEntry(tournamentModel.getId(), userModel.getUserID(), "like", new OnAddLikeDislikeListener() {
//                        @Override
//                        public void onAddLikeDislikeSuccess() {
//                            changeLikeIcon(true);
//                            totalLikeCount++;
//                            updateLikeUi();
//                            binding.ivBtnDislike.setEnabled(false);
//                            binding.ivBtnLike.setEnabled(false);
//                        }
//
//                        @Override
//                        public void onAddLikeDislikeError(String message) {
//
//                        }
//                    });
//                }else {
//
//                    String tag = binding.tvStatusLable.getText().toString();
//                    ReusableAlertDialog.showConfirmationDialog(UserTournamentDetailsPage.this, "This event is " + tag + ".", "You Can Only Join or start Current Tournaments", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//                }

            }
        });

        binding.ivBtnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (isCurrent){
//                    quizConroller.addLikeDislikeEntry(tournamentModel.getId(), userModel.getUserID(), "dislike", new OnAddLikeDislikeListener() {
//                        @Override
//                        public void onAddLikeDislikeSuccess() {
//                            changeDislikeIcon(true);
//                            totalDislikeCount++;
//                            updateLikeUi();
//                            binding.ivBtnLike.setEnabled(false);
//                        }
//
//                        @Override
//                        public void onAddLikeDislikeError(String message) {
//
//                        }
//                    });
//                }else {
//
//                    String tag = binding.tvStatusLable.getText().toString();
//                    ReusableAlertDialog.showConfirmationDialog(UserTournamentDetailsPage.this, "This event is " + tag + ".", "You Can Only Join or start Current Tournaments", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPlayedData();
        getTotalLikeCount();
        getLikeData();
    }

    void getTotalLikeCount() {
        quizConroller.getQuizLikeByTournamentId(tournamentModel.getId(), new OnGetLikeDislikeListListener() {
            @Override
            public void onGetLikeDislikeList(List<QuizLikeModel> likeDislikeList) {
                Log.e(TAG, "onGetLikeDislikeList: " + likeDislikeList.toString());
                if (!likeDislikeList.isEmpty()) {
                    for (QuizLikeModel q : likeDislikeList) {
                        if (q.getType().equals("like")) {
                            totalLikeCount += 1;
                        } else if (q.getType().equals("dislike")) {
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

    void getLikeData() {
        quizConroller.getQuizLikeDislikeByTournamentIdAndUserId(tournamentModel.getId(), userModel.getUserID(), new OnGetLikeDislikeListListener() {
            @Override
            public void onGetLikeDislikeList(List<QuizLikeModel> likeDislikeList) {
                Log.e(TAG, "onGetLikeDislikeList: " + likeDislikeList.toString());
                if (!likeDislikeList.isEmpty()) {
                    quizLikeModel = likeDislikeList.get(0);
                    if (quizLikeModel.getType().equals("like")) {
                        changeLikeIcon(true);

                    } else if (quizLikeModel.getType().equals("dislike")) {
                        changeDislikeIcon(true);
                    }
                    binding.ivBtnDislike.setEnabled(false);
                    binding.ivBtnLike.setEnabled(false);

                }
            }

            @Override
            public void onGetError(String message) {
                Log.e(TAG, "onGetError: " + message);
            }
        });
    }

    //function to change image icon on like and dislike
    void changeLikeIcon(boolean isLike) {
        if (isLike) {
            binding.ivBtnLike.setImageResource(R.drawable.like_fill); // Set liked image
        } else {
            binding.ivBtnLike.setImageResource(R.drawable.like); // Set unliked image
        }
    }

    void changeDislikeIcon(boolean isLike) {
        if (isLike) {
            binding.ivBtnDislike.setImageResource(R.drawable.dislike_fill); // Set liked image
        } else {
            binding.ivBtnDislike.setImageResource(R.drawable.dislike); // Set unliked image
        }
    }

    void updateLikeUi() {
        binding.tvLikeCount.setText(String.format("%d", totalLikeCount));
        binding.tvDislikeCount.setText(String.format("%d", totalDislikeCount));
    }


    void getPlayedData() {
        showProgress(true);
        try {
            quizConroller.getQuizPlayedModelByUserIdAndTournamentId(userModel.getUserID(), tournamentModel.getId(), new OnGetQuizPlayedModelListener() {
                @Override
                public void onGetQuizPlayedModel(QuizPlayedModel quizPlayedModel) {
                    showProgress(false);
                    if (quizPlayedModel != null) {

                        String date = HelperUtils.convertDateToStr(quizPlayedModel.getPlayedDate());
                        binding.tvQuizDate.setText(date);
                        binding.tvQuizScore.setText("" + quizPlayedModel.getScore());
                        showStartButton(false);
                    } else {
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
        } catch (Exception e) {
            Log.e(TAG, "getPlayedData: " + e.getMessage());
        }

    }

    void setData() {
        binding.tvTitle.setText(tournamentModel.getTitle());
        binding.tvCategory.setText(tournamentModel.getCategoryName());
        binding.tvstartDate.setText(tournamentModel.getStartDate());
        binding.tvEndDate.setText(tournamentModel.getEndDate());
        binding.tvDifficulty.setText(tournamentModel.getDifficulty().toUpperCase());
        try {
            getTournamentTag(tournamentModel);
        } catch (Exception e) {
            Log.e(TAG, "setData: TSg Error " + e.getMessage());
        }
    }

    void showProgress(boolean show) {
        if (show) {
            binding.lvDetailsView.setVisibility(View.GONE);
            binding.progressCircularView.setVisibility(View.VISIBLE);
        } else {
            binding.lvDetailsView.setVisibility(View.VISIBLE);
            binding.progressCircularView.setVisibility(View.GONE);
        }
    }

    void showStartButton(boolean isShow) {
        if (isShow) {
            binding.lvPlayedQuizView.setVisibility(View.GONE);
            binding.btnStartQuiz.setVisibility(View.VISIBLE);

        } else {
            binding.lvPlayedQuizView.setVisibility(View.VISIBLE);
            binding.btnStartQuiz.setVisibility(View.GONE);
        }
    }

    private void getTournamentTag(TournamentModel tournament) throws ParseException {
        String tag = tournament.getTag(new Date());
        binding.tvStatusLable.setText(tag);
        if (tag.equals(AppString.strOngoing)) {
            isCurrent = true;

        } else {
            isCurrent = false;

        }

    }

}