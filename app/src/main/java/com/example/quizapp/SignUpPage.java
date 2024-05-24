package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizapp.databinding.ActivitySignUpPageBinding;
import com.example.quizapp.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpPage extends AppCompatActivity {

    ImageView BackButton;

    private static final String TAG = "SignUpPage";
    private FirebaseAuth mAuth;

    FirebaseFirestore db ;

 private ActivitySignUpPageBinding binding;
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
          //  reload();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivitySignUpPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
       db = FirebaseFirestore.getInstance();



        binding.backIcon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        binding.btnSignup.setOnClickListener(v -> {
            if (binding.etName.getText().toString().isEmpty()){
                errorMsg("Enter Name");
            }
            else if (binding.etEmail.getText().toString().isEmpty()){
                errorMsg("Enter Email");
            } else if (binding.etPassword.getText().toString().isEmpty()) {
                errorMsg("Enter Password");
            } else{
                try {
                    createAccount(binding.etEmail.getText().toString().trim(), binding.etPassword.getText().toString().trim());

                }catch (Exception e){
                    Log.e(TAG, "onCreate: " + e );
                }
            }
        });

    }


    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        showProgree(true);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                            if (user != null){
                                UserModel myUser = new UserModel(user.getUid(), binding.etName.getText().toString().trim(), binding.etMobile.getText().toString().trim(),user.getEmail(),"user");
                                db.collection("users").document(myUser.getUserID()).set(myUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                showProgree(false);
                                                Log.e(TAG, "DocumentSnapshot successfully written!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "onFailure: " + e.getMessage() );
                                        showProgree(false);
                                    }
                                });
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpPage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    void  updateUI(FirebaseUser user){
        if (user != null){


        }
    }

   void  errorMsg(String msg){
        Toast.makeText(SignUpPage.this, msg,
                Toast.LENGTH_SHORT).show();
    }

   void showProgree(boolean show){
        if (show){
            binding.btnSignup.setVisibility(View.GONE);
            binding.progressCircularView.setVisibility(View.VISIBLE);
        }else {
            binding.btnSignup.setVisibility(View.VISIBLE);
            binding.progressCircularView.setVisibility(View.GONE);
        }
    }
}