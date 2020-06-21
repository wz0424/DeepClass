package com.example.deepclass.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepclass.databasectrl.UserInfo;
import com.example.deepclass.main.R;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHodler> {
    List<UserInfo> userInfos = new ArrayList<>();
    public void setUserInfos(List<UserInfo> userInfos){
        this.userInfos = userInfos;
    }

    @NonNull
    @Override
    public LeaderboardViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_time,parent,false);
        return new LeaderboardViewHodler(itemView);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHodler holder, int position) {
        UserInfo userInfo = userInfos.get(position);
        holder.textViewRank.setText(String.valueOf(position+1));
        holder.textViewNum.setText(userInfo.getNum());
        holder.textViewName.setText(userInfo.getName());
        holder.textViewTime.setText(String.valueOf(userInfo.getTotalTime())+"分钟");
    }

    @Override
    public int getItemCount() {
        return userInfos.size();
    }

    static class LeaderboardViewHodler extends RecyclerView.ViewHolder{
        TextView textViewRank,textViewNum,textViewName,textViewTime;

        public LeaderboardViewHodler(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewNum = itemView.findViewById(R.id.textViewNum);
            textViewRank = itemView.findViewById(R.id.textViewRank);
            textViewTime = itemView.findViewById(R.id.textViewTime);
        }

    }
}
