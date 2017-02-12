package com.example.weina.bishe.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weina.bishe.R;

import java.util.ArrayList;

/**
 * Created by weina on 2017/2/11.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{
    public ArrayList<String> datas = null;
    public HomeAdapter(ArrayList<String> datas) {
        this.datas = datas;
    }
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(HomeAdapter.ViewHolder viewHolder, int position) {
        viewHolder.mTextView.setText(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ViewHolder(View view){
            super(view);
            mTextView = (TextView) view.findViewById(R.id.text);
        }
    }
}
