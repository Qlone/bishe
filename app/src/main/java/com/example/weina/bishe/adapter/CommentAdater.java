package com.example.weina.bishe.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weina.bishe.R;
import com.example.weina.bishe.entity.CommentEntity;
import com.example.weina.bishe.util.view.ScoreView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by weina on 2017/3/30.
 */
public class CommentAdater extends  RecyclerView.Adapter<CommentAdater.ViewHolder>{
    private ArrayList<CommentEntity> data;
    public CommentAdater(ArrayList<CommentEntity> datas) {
        this.data = datas;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh ;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mContext.setText(data.get(position).getContext());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        holder.mTime.setText("评论时间: "+sdf.format(data.get(position).getCommentData()));
        String name = data.get(position).getUserName().substring(0,2)+"***";
        holder.mName.setText(name);
        holder.mScoreView.setScoreInit(data.get(position).getStart());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mName;
        public TextView mTime;
        public ScoreView mScoreView;
        public TextView mContext;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.comment_item_name);
            mTime = (TextView) itemView.findViewById(R.id.comment_item_time);
            mContext = (TextView) itemView.findViewById(R.id.comment_item_context);
            mScoreView = (ScoreView) itemView.findViewById(R.id.comment_item_score);
            mScoreView.setTouchUse(false);//禁止触摸
        }
    }
}
