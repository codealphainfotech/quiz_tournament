package com.example.quizapp.HomepageCards;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quizapp.R;
import com.example.quizapp.adapters.UserListADapter;
import com.example.quizapp.databinding.ActivityUsersPageBinding;
import com.example.quizapp.models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class UsersPage extends AppCompatActivity {
    FirebaseFirestore db ;
    private static final String TAG = "UsersPage";

    private ActivityUsersPageBinding binding;

    List<UserModel> users ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityUsersPageBinding.inflate(getLayoutInflater());

        binding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setContentView(binding.getRoot());
        db  = FirebaseFirestore.getInstance();

        getDataFromDb();

    }

    void getDataFromDb(){

        showProgree(true);
        db.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                showProgree(false);
                Log.e(TAG, "onSuccess: "+ queryDocumentSnapshots.getDocuments().toString() );

                if (queryDocumentSnapshots.isEmpty()) {
                    Log.d(TAG, "onSuccess: LIST EMPTY");
                    return;
                } else {
                    users = queryDocumentSnapshots.toObjects(UserModel.class);
                    initRv();
                    // Add all to your list
                    Log.e(TAG, "onSuccess: " + users);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showProgree(false);
                Log.e(TAG, "onFailure: "+ e.getMessage() );
            }
        });
    }

    void initRv(){
        UserListADapter adapter = new UserListADapter((ArrayList<UserModel>) users, this);
        binding.rvUsers.setLayoutManager(new LinearLayoutManager(this));
        binding.rvUsers.setAdapter(adapter);
    }

    void showProgree(boolean show){
        if (show){
            binding.userView.setVisibility(View.GONE);
            binding.progressCircularView.setVisibility(View.VISIBLE);
        }else {
            binding.userView.setVisibility(View.VISIBLE);
            binding.progressCircularView.setVisibility(View.GONE);
        }
    }
}