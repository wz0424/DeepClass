package com.example.deepclass.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.deepclass.main.R;
import com.example.deepclass.myutil.QuestionBean;

import java.util.List;

public class QuestionListAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private List<QuestionBean> data;


//    public QustionListAdapter(Context context){
//        mLayoutInflater = LayoutInflater.from(context);
//    }

//    public QustionListAdapter(Context context, List<QuestionBean> data){
//        mLayoutInflater = LayoutInflater.from(context);
//        this.data=data;
//    }
    public QuestionListAdapter(Context context,List<QuestionBean> data){
        mLayoutInflater=LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public int getCount() {
        //总共呈现几条题目
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data == null ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder{
        private ImageView mIvQstFolder;
        private TextView mTvQstTitle;
        private TextView mTvQstDesp;
    }

    @SuppressLint({"InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final QuestionBean questionBean = data.get(position);

        ViewHolder holder = null;
        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.layout_list_item, null);
            holder = new ViewHolder();

            holder.mIvQstFolder = convertView.findViewById(R.id.iv_qst_folder);
            holder.mTvQstTitle = convertView.findViewById(R.id.tv_title);
            holder.mTvQstDesp = convertView.findViewById(R.id.tv_desp);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

//        String titleAndId = questionBean.getId() +"、" + questionBean.getQstTitle();
        holder.mTvQstTitle.setText(questionBean.getQstTitle());
        holder.mTvQstDesp.setText(questionBean.getQstDesp());

        //固定每个list的长度和宽度
        ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,210);
        convertView.setLayoutParams(params);

        return convertView;
    }

}
