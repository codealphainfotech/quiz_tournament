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
import com.example.quizapp.contoller.OnSaveRecordListener;
import com.example.quizapp.contoller.QuizConroller;
import com.example.quizapp.contoller.VolleyCallback;
import com.example.quizapp.databinding.ActivityUserQuestionPageBinding;
import com.example.quizapp.databinding.ActivityUserQuizPageBinding;
import com.example.quizapp.models.ApiResponse;
import com.example.quizapp.models.QuizPlayedModel;
import com.example.quizapp.models.TournamentModel;
import com.example.quizapp.models.UserModel;
import com.example.quizapp.utils.HelperUtils;
import com.example.quizapp.utils.ReusableAlertDialog;
import com.example.quizapp.utils.SharedPrefsHelper;
import com.example.quizapp.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class UserQuizPage extends AppCompatActivity {

    private String TAG = "UserQuizPage";

    ImageView backIcon;

    private ActivityUserQuizPageBinding binding;
    private QuizConroller quizConroller;

    private int currentQuiz = 0;
    private String tournamentID = "";
    private TournamentModel tournamentModel;

    private List<ApiResponse.Result> quizList = new ArrayList<>();
    private List<String> ansList = new ArrayList<>();

    private SharedPrefsHelper sharedPrefsHelper;


    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUserQuizPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        quizConroller = new QuizConroller();

        sharedPrefsHelper = new SharedPrefsHelper(this);

        Intent i = getIntent();
        if (i.getExtras() != null) {
            tournamentID = i.getStringExtra("id");
            Log.e(TAG, "onCreate: id :" + tournamentID);
            Log.e(TAG, "onCreate: " + i.getStringExtra("model"));

            tournamentModel = quizConroller.decodeTournamentFromJson(i.getStringExtra("model"));

            if (tournamentModel != null) {
                String url = " https://opentdb.com/api.php?amount=10";
                url += "&category=" + tournamentModel.getCategoryId();
                if (!tournamentModel.getDifficulty().equals("Any Difficulty")) {
                    url += "&difficulty=" + tournamentModel.getDifficulty().toLowerCase();
                }

                showProgress(true);
                quizConroller.getQuizQuestions(this, url, new VolleyCallback() {
                    @Override
                    public void onSuccess(List<ApiResponse.Result> results) {
                        showProgress(false);
                        quizList = results;
                        setData();
                    }

                    @Override
                    public void onError(String message) {
                        showProgress(false);
                    }
                });
            }
        }


        backIcon = findViewById(R.id.backIcon);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.lvBtnOPT1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRightAns(binding.tvOPT1.getText().toString(), 1);
            }
        });

        binding.lvBtnOPT2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRightAns(binding.tvOPT2.getText().toString(), 2);

            }
        });

        binding.lvBtnOPT3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRightAns(binding.tvOPT3.getText().toString(), 3);

            }
        });

        binding.lvBtnOPT4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRightAns(binding.tvOPT4.getText().toString(), 4);

            }
        });

        binding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToNext();
            }
        });

        binding.tvQueRightCount.setText(String.format("Right Ans : %d / 10", score));

        binding.btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGameOverView(true);
            }
        });

        binding.ivBtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizConroller.addLikeDislikeEntry(tournamentModel.getId(), sharedPrefsHelper.getUserModelFromSharedPref().getUserID(), "like", new OnAddLikeDislikeListener() {
                    @Override
                    public void onAddLikeDislikeSuccess() {
                        changeLikeIcon(true);
                        binding.ivBtnDislike.setEnabled(false);
                        binding.ivBtnLike.setEnabled(false);
                    }

                    @Override
                    public void onAddLikeDislikeError(String message) {

                    }
                });

            }
        });

        binding.ivBtnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                quizConroller.addLikeDislikeEntry(tournamentModel.getId(), sharedPrefsHelper.getUserModelFromSharedPref().getUserID(), "dislike", new OnAddLikeDislikeListener() {
                    @Override
                    public void onAddLikeDislikeSuccess() {
                        changeDislikeIcon(true);
                        binding.ivBtnLike.setEnabled(false);
                    }

                    @Override
                    public void onAddLikeDislikeError(String message) {

                    }
                });


            }
        });
    }

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

    private void checkRightAns(String ans, int index) {
        ApiResponse.Result que = quizList.get(currentQuiz);

        //setting right ans
        binding.tvRightAns.setText(String.format("Correct Ans : %s", que.getCorrectAnswer()));
        binding.tvYourAns.setText(String.format("Your Ans : %s", ans));

        showAnsView(true);

        if (que.getCorrectAnswer().equals(ans)) {
            ToastUtils.successToast(UserQuizPage.this, "Correct Answer");
            updateScore();
        } else {
            ToastUtils.errorToast(UserQuizPage.this, "Your Answer is Wrong");
        }
        int pos = ansList.indexOf(que.getCorrectAnswer());
        Log.e(TAG, "checkRightAns: pos " + pos);
        if (pos != -1) {
            setCorrectImageViewVisible(pos + 1);
        }
        currentQuiz++;

        if (currentQuiz < 10) {
            showNextBtn(true);
        } else {
            showFinish(true);
            String pId = HelperUtils.generateUniqueID();
            UserModel user = sharedPrefsHelper.getUserModelFromSharedPref();
            QuizPlayedModel quizPlayedModel = new QuizPlayedModel(pId, tournamentModel, user, new Date(), score);
            quizConroller.saveQuizPlayedRecord(quizPlayedModel, new OnSaveRecordListener() {
                @Override
                public void onSaveSuccess(String message) {
                    ToastUtils.successToast(UserQuizPage.this, "Quiz Finished");

                }

                @Override
                public void onSaveError(String message) {
                    ToastUtils.errorToast(UserQuizPage.this, message);
                }
            });
        }

    }

    private void updateScore() {
        score += 1;
        binding.tvQueRightCount.setText(String.format("Right Ans : %d / 10", score));
        binding.tvGameOverScore.setText(String.format("Right Ans : %d / 10", score));
        binding.tvGameOverTitle.setText(tournamentModel.getTitle());

    }


    private void moveToNext() {
        showNextBtn(false);
        if (currentQuiz < 10) {
            setData();
        } else {
            showFinish(true);
            ToastUtils.successToast(UserQuizPage.this, "Quiz Finished");
        }
    }

    void setData() {
        //reset ui
        resetAnsPanel();
        showNextBtn(false);

        binding.tvQueInfoCount.setText(String.format("Question : %d / 10", currentQuiz + 1));

        if (!quizList.isEmpty()) {
            ApiResponse.Result que = quizList.get(currentQuiz);
            binding.tvQuizCount.setText(String.format("Que No : %d", currentQuiz + 1));
            binding.tvQuizTitle.setText(que.getQuestion());

            ansList = que.getIncorrectAnswers();
            ansList.add(que.getCorrectAnswer());
            ansList = shuffleAnswers(ansList);

            if (que.getType().equals("multiple")) {
                setAnsUi();

            } else {
                setAnsUiOption2();
            }
        }


    }

    void resetAnsPanel() {
        binding.tvOPT1.setText("");
        binding.tvOPT2.setText("");
        binding.tvOPT3.setText("");
        binding.tvOPT4.setText("");

        binding.ivCorrect1.setVisibility(View.GONE);
        binding.ivCorrect2.setVisibility(View.GONE);
        binding.ivCorrect3.setVisibility(View.GONE);
        binding.ivCorrect4.setVisibility(View.GONE);

        showAnsView(false);
    }

    public void setAnsUi() {

        binding.tvOPT1.setText(ansList.get(0));
        binding.tvOPT2.setText(ansList.get(1));
        binding.tvOPT3.setText(ansList.get(2));
        binding.tvOPT4.setText(ansList.get(3));
        binding.lvBtnOPT3.setVisibility(View.VISIBLE);
        binding.lvBtnOPT4.setVisibility(View.VISIBLE);

    }

    public void setAnsUiOption2() {
        binding.tvOPT1.setText(ansList.get(0));
        binding.tvOPT2.setText(ansList.get(1));
        binding.lvBtnOPT3.setVisibility(View.GONE);
        binding.lvBtnOPT4.setVisibility(View.GONE);
    }

    public void showNextBtn(boolean isShow) {
        if (isShow) {
            binding.btNext.setVisibility(View.VISIBLE);
        } else {
            binding.btNext.setVisibility(View.GONE);
        }
    }

    public void showFinish(boolean isShow) {
        if (isShow) {
            binding.btnFinish.setVisibility(View.VISIBLE);
        } else {
            binding.btnFinish.setVisibility(View.GONE);
        }
    }

    void showGameOverView(boolean isShow) {
        if (isShow) {
            binding.btnFinish.setVisibility(View.GONE);
            binding.lvQuizView.setVisibility(View.GONE);
            binding.lvGameOverView.setVisibility(View.VISIBLE);
        } else {
            binding.lvGameOverView.setVisibility(View.GONE);
        }
    }

    public void setCorrectImageViewVisible(int position) {
        if (position < 1 || position > 4) {
            throw new IllegalArgumentException("Invalid position: " + position);
        }

        switch (position) {
            case 1:
                binding.ivCorrect1.setVisibility(View.VISIBLE);
                binding.ivCorrect2.setVisibility(View.GONE);
                binding.ivCorrect3.setVisibility(View.GONE);
                binding.ivCorrect4.setVisibility(View.GONE);
                break;
            case 2:
                binding.ivCorrect1.setVisibility(View.GONE);
                binding.ivCorrect2.setVisibility(View.VISIBLE);
                binding.ivCorrect3.setVisibility(View.GONE);
                binding.ivCorrect4.setVisibility(View.GONE);
                break;
            case 3:
                binding.ivCorrect1.setVisibility(View.GONE);
                binding.ivCorrect2.setVisibility(View.GONE);
                binding.ivCorrect3.setVisibility(View.VISIBLE);
                binding.ivCorrect4.setVisibility(View.GONE);
                break;
            case 4:
                binding.ivCorrect1.setVisibility(View.GONE);
                binding.ivCorrect2.setVisibility(View.GONE);
                binding.ivCorrect3.setVisibility(View.GONE);
                binding.ivCorrect4.setVisibility(View.VISIBLE);
                break;
        }
    }


    void showAnsView(boolean show) {
        if (show) {
            binding.tvYourAns.setVisibility(View.VISIBLE);
            binding.tvRightAns.setVisibility(View.VISIBLE);
        } else {
            binding.tvYourAns.setVisibility(View.GONE);
            binding.tvRightAns.setVisibility(View.GONE);
        }
    }

    void showProgress(boolean show) {
        if (show) {
            binding.lvQuizView.setVisibility(View.GONE);
            binding.progressCircularView.setVisibility(View.VISIBLE);
        } else {
            binding.lvQuizView.setVisibility(View.VISIBLE);
            binding.progressCircularView.setVisibility(View.GONE);
        }
    }

    public static List<String> shuffleAnswers(List<String> answerList) {
        if (answerList.size() <= 1) {
            return answerList; // No need to shuffle if there's only one answer
        }

        List<String> shuffledAnswers = new ArrayList<>(answerList);
        Collections.shuffle(shuffledAnswers);

        // Assuming the first element is always the correct answer
        String correctAnswer = shuffledAnswers.get(0);
        shuffledAnswers.remove(0); // Remove the correct answer

        // Add the correct answer back to a random position
        int randomIndex = new Random().nextInt(shuffledAnswers.size());
        shuffledAnswers.add(randomIndex, correctAnswer);

        return shuffledAnswers;
    }
}