package com.example.quizapp.tournament;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.quizapp.R;
import com.example.quizapp.adapters.CategoryAdapter;
import com.example.quizapp.adapters.DifficultyAdapter;
import com.example.quizapp.contoller.OnFailed;
import com.example.quizapp.contoller.OnSuccessListner;
import com.example.quizapp.contoller.QuizConroller;
import com.example.quizapp.databinding.ActivityCreateNewTurnamenrBinding;
import com.example.quizapp.models.CategoryModel;
import com.example.quizapp.models.TournamentModel;
import com.example.quizapp.utils.AppString;
import com.example.quizapp.utils.HelperUtils;
import com.example.quizapp.utils.ToastUtils;

import java.util.Calendar;

public class CreateNewTournament extends AppCompatActivity {

    ImageView backIcon;

    private static ActivityCreateNewTurnamenrBinding binding;

    private QuizConroller quizConroller;

    private  String TAG = "CreateNewTournament";

    private int selectedCat = 0;
    private  int selectedDifficulty = 0;

    private Calendar startCal = Calendar.getInstance();
    private Calendar endCal = Calendar.getInstance();

    private TournamentModel tournamentModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityCreateNewTurnamenrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        quizConroller = new QuizConroller();

        backIcon = findViewById(R.id.backIcon);

        /*

         Intent editIntent = new Intent(TournamentDetailsPage.this, CreateNewTournament.class);
            editIntent.putExtra("model", tournamentModel.toJsonString());
         */

        Intent i = getIntent();
        if (i.getExtras() != null){

            Log.e(TAG, "onCreate: " + i.getStringExtra("model"));

            tournamentModel = quizConroller.decodeTournamentFromJson(i.getStringExtra("model"));


        }
        if (tournamentModel != null){
            setData();
        }else{
            setCurrentDateToTextView(binding.tvStartDate);
            setCurrentDateToTextView(binding.tvEndDate);
        }


        // Set up the category spinner
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, quizConroller.getCategories());
        binding.spinnerCategory.setAdapter(categoryAdapter);
        binding.spinnerCategory.setSelection(selectedCat);

        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onCategorySelected: Selected Cate3 :: " + position );
                selectedCat = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DifficultyAdapter difficultyAdapter = new DifficultyAdapter(this, quizConroller.getDifficultyList());
        binding.spinnerDifficulty.setAdapter(difficultyAdapter);
        binding.spinnerDifficulty.setSelection(selectedDifficulty);


        binding.spinnerDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "onDifficulty: Selected :: " + position );
                selectedDifficulty = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.etQuizTitle.getText().toString().isEmpty()){
                    ToastUtils.errorToast(CreateNewTournament.this, "Enter Quiz Title");
                }else {

                    CategoryModel categoryModel = quizConroller.getCategories().get(selectedCat);
                    String difficulty = quizConroller.getDifficultyList().get(selectedDifficulty);
                    String mID  =  "";
                    if (tournamentModel != null){
                        mID = tournamentModel.getId();
                    }else {
                      mID =  HelperUtils.generateUniqueID() ;
                    }
                    TournamentModel newTournamentModel = new TournamentModel(mID, binding.etQuizTitle.getText().toString().trim(), binding.tvStartDate.getText().toString(), binding.tvEndDate.getText().toString(),categoryModel.getName(),categoryModel.getId(),difficulty,"any");

                    showProgress(true);
                    quizConroller.createQuiz(newTournamentModel, new OnSuccessListner() {
                        @Override
                        public void onSuccess() {
                            showProgress(false);
                            if (tournamentModel != null){
                                ToastUtils.successToast(CreateNewTournament.this, AppString.strTournament + " Updated");
                                Intent resutlIntent = new Intent();
                                resutlIntent.putExtra("model", newTournamentModel.toJsonString());
                                setResult(123, resutlIntent);
                                finish();
                            }else{
                                ToastUtils.successToast(CreateNewTournament.this, AppString.strTournament + " Created");
                                finish();
                            }

                        }
                    }, new OnFailed() {
                        @Override
                        public void onFailed() {
                            showProgress(false);
                            ToastUtils.errorToast(CreateNewTournament.this, AppString.strSomethingWent);
                        }
                    });

                }

            }
        });

        binding.ivBtnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showTruitonDatePickerDialog(v);
            }
        });

        binding.ivBtnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToDatePickerDialog(v);
            }
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    void setData(){
        if (tournamentModel != null){
            binding.etQuizTitle.setText(tournamentModel.getTitle());
           binding.tvStartDate.setText(tournamentModel.getStartDate());
           binding.tvEndDate.setText(tournamentModel.getEndDate());
           CategoryModel categoryModel = new CategoryModel(tournamentModel.getCategoryId(), tournamentModel.getCategoryName());
           int pos = -1;
            for (int i = 0; i < quizConroller.getCategories().size(); i++) {
                Log.e(TAG, "setData: Cat IDs :" + quizConroller.getCategories().get(i).getId());
                if (quizConroller.getCategories().get(i).getId() == tournamentModel.getCategoryId() ){
                    pos = i;
                    break;
                }
            }

           if (pos != -1){
               selectedCat = pos;
               Log.e(TAG, "setData: selectedCat :" + selectedCat );

           }

           int difiPos = quizConroller.getDifficultyList().indexOf(tournamentModel.getDifficulty());
           if (difiPos != -1){
               selectedDifficulty = difiPos;
           }
           binding.btSave.setText("Update");
           binding.tvTitleBar.setText("Update Tournament");
        }
    }

    void showProgress(boolean show){
        if (show){
            binding.btSave.setVisibility(View.GONE);
            binding.progressCircularView.setVisibility(View.VISIBLE);
        }else {
            binding.btSave.setVisibility(View.VISIBLE);
            binding.progressCircularView.setVisibility(View.GONE);
        }
    }

    void setCurrentDateToTextView(TextView view){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        String date = day + "/" + (month + 1) + "/" + year;
        view.setText(date);
    }

    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showToDatePickerDialog(View v) {
        DialogFragment newFragment = new ToDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    public static   class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(getActivity(),this, year,
                    month,day);
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            binding.tvStartDate.setText(day + "/" + month  + "/" + year);
        }

    }

    public  static class ToDatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        // Calendar startDateCalendar=Calendar.getInstance();
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            String getfromdate = binding.tvStartDate.getText().toString().trim();
            String getfrom[] = getfromdate.split("/");
            int year,month,day;
            year= Integer.parseInt(getfrom[2]);
            month = Integer.parseInt(getfrom[1]);
            day = Integer.parseInt(getfrom[0]);
            final Calendar c = Calendar.getInstance();
            c.set(year,month,day+1);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),this, year,month,day);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            return datePickerDialog;
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {

            binding.tvEndDate.setText(day + "/" + month  + "/" + year);
        }
    }

}