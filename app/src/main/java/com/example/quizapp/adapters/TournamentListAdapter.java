package com.example.quizapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.customer.UserTournamentDetailsPage;
import com.example.quizapp.databinding.CardQuizItemBinding;
import com.example.quizapp.models.TournamentModel;
import com.example.quizapp.tournament.TournamentDetailsPage;
import com.google.android.material.tabs.TabLayout;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TournamentListAdapter  extends RecyclerView.Adapter<TournamentListAdapter.ViewHolder> {

    List<TournamentModel> mlist;
    Context context;

    boolean isAdmin ;

    public TournamentListAdapter(Context context, List<TournamentModel> tournamentModels, boolean isAdmin) {
        this.mlist = tournamentModels;
        this.context = context;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public TournamentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new TournamentListAdapter.ViewHolder(CardQuizItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TournamentModel tournamentModel = mlist.get(position);


        int index = position;
        holder.binding.tvTitle.setText(tournamentModel.getTitle());
        String dateSting = "Date : " + tournamentModel.getStartDate() + " to " + tournamentModel.getEndDate();
        holder.binding.tvStartDate.setText(dateSting);

        try {
            holder.binding.tvStatus.setText(getTournamentTag(tournamentModel));

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAdmin){
                    Intent i = new Intent(context, TournamentDetailsPage.class);
                    i.putExtra("index", index);
                    i.putExtra("id", tournamentModel.getId());
                    i.putExtra("model", tournamentModel.toJsonString());
                    context.startActivity(i);
                }else {
                    Intent i = new Intent(context, UserTournamentDetailsPage.class);
                    i.putExtra("index", index);
                    i.putExtra("id", tournamentModel.getId());
                    i.putExtra("model", tournamentModel.toJsonString());
                    context.startActivity(i);
                }

            }
        });

    }
     public void updateList(List<TournamentModel> list){
        mlist = list;
        notifyDataSetChanged();
     }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{

        CardQuizItemBinding binding;
        TabLayout tabLayout;
        public ViewHolder(@NonNull CardQuizItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;




        }
    }

    private String getTournamentTag(TournamentModel tournament) throws ParseException {
        return tournament.getTag(new Date()); // Assuming you have current date
    }
}
