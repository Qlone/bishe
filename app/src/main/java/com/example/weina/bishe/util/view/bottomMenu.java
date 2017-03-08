package com.example.weina.bishe.util.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.weina.bishe.R;

/**
 * Created by weina on 2017/3/8.
 */
public class bottomMenu extends LinearLayout implements View.OnClickListener{
    private Context mContext;

    public bottomMenu(Context context) {
        super(context);
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.me_main_layout, this);
    }

    @Override
    public void onClick(View view) {

    }
}
