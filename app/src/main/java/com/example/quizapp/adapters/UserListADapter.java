package com.example.quizapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.ShowAUserProfile;
import com.example.quizapp.databinding.CardUserBinding;
import com.example.quizapp.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserListADapter extends RecyclerView.Adapter<UserListADapter.ViewHolder> {

    ArrayList<UserModel> userList;
    Context context;

    public UserListADapter(ArrayList<UserModel> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CardUserBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserModel user = userList.get(position);
        holder.binding.tvUserName.setText(user.getName()); ;
        holder.binding.tvUserEmail.setText(user.getEmail());

        holder.binding.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowAUserProfile.class);
                intent.putExtra("userName", user.getName()); // Pass data if needed
                intent.putExtra("userEmail", user.getEmail()); // Pass data if needed
                intent.putExtra("userPhone", user.getPhone()); // Pass data if needed
               // Pass data if needed
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{

        CardUserBinding binding;
        public ViewHolder(@NonNull CardUserBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }
}
