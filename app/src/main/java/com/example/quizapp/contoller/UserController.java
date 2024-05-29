package com.example.quizapp.contoller;

import com.example.quizapp.models.UserModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserController {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userPath = "users";

    public  UserModel currentUser;

    public void getUserById(String userId, OnGetUserListner listener) {

        DocumentReference userRef = db.collection("users").document(userId);

        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        UserModel user = documentSnapshot.toObject(UserModel.class);
                        if (user != null) {
                            currentUser = user;
                            listener.onGetUserSuccess(user);
                        } else {
                            listener.onGetUserError("Error parsing user data");
                        }
                    } else {
                        listener.onGetUserError("User not found");
                    }
                })
                .addOnFailureListener(e -> listener.onGetUserError("Error getting user: " + e.getMessage()));
    }
}
