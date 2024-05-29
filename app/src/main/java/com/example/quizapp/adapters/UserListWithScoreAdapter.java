package com.example.quizapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.ShowAUserProfile;
import com.example.quizapp.databinding.CardUserWithScoreBinding;
import com.example.quizapp.databinding.CardUserWithScoreBinding;
import com.example.quizapp.models.QuizPlayedModel;
import com.example.quizapp.models.UserModel;

import java.util.ArrayList;

public class UserListWithScoreAdapter extends RecyclerView.Adapter<UserListWithScoreAdapter.ViewHolder> {

    ArrayList<QuizPlayedModel> playedModels;

    Context context;


    public UserListWithScoreAdapter(ArrayList<QuizPlayedModel> playedModels, Context context) {
        this.playedModels = playedModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CardUserWithScoreBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserModel user = playedModels.get(position).getUserModel();
        holder.binding.tvUserName.setText(user.getName()); ;
        holder.binding.tvUserEmail.setText(user.getEmail());
        holder.binding.tvGameScore.setText("" + playedModels.get(position).getScore());
    }

    @Override
    public int getItemCount() {
        return playedModels.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{

        CardUserWithScoreBinding binding;
        public ViewHolder(@NonNull CardUserWithScoreBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }
}
