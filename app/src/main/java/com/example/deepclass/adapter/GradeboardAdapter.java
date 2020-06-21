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

public class GradeboardAdapter extends RecyclerView.Adapter<GradeboardAdapter.GradeboardViewHodler> {
    List<UserInfo> userInfos = new ArrayList<>();
    public void setUserInfos(List<UserInfo> userInfos){
        this.userInfos = userInfos;
    }

    @NonNull
    @Override
    public GradeboardViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_grade,parent,false);
        return new GradeboardViewHodler(itemView);
        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull GradeboardViewHodler holder, int position) {
        UserInfo userInfo = userInfos.get(position);
        holder.textViewRank.setText(String.valueOf(position+1));
        holder.textViewNum.setText(userInfo.getNum());
        holder.textViewName.setText(userInfo.getName());
        holder.textViewGrade.setText(String.valueOf(userInfo.getGrade())+"分");
    }

    @Override
    public int getItemCount() {
        return userInfos.size();
    }

    static class GradeboardViewHodler extends RecyclerView.ViewHolder{
        TextView textViewRank,textViewNum,textViewName,textViewGrade;

        public GradeboardViewHodler(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewNum = itemView.findViewById(R.id.textViewNum);
            textViewRank = itemView.findViewById(R.id.textViewRank);
            textViewGrade = itemView.findViewById(R.id.textViewGrade);
        }

    }
}
