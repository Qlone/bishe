package com.example.weina.bishe.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.weina.bishe.R;
import com.example.weina.bishe.bean.MenuList;

import java.util.List;

/**
 * Created by weina on 2017/3/7.
 */
public class MenuAdapter extends ArrayAdapter<MenuList> {
    private int resourceId;
    private Context mContext;
    public MenuAdapter(Context context, int textViewResourceId, List<MenuList> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        mContext =context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        MenuList menuList = getItem(position); // 获取当前项的Fruit实例
        TextView fruitName = (TextView) view.findViewById(R.id.menu_text);
        Drawable drawable= ContextCompat.getDrawable(mContext,menuList.getDrawAble());

/// 这一步必须要做,否则不会显示.

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        fruitName.setCompoundDrawables(drawable,null,null,null);
        //绑定数据
        fruitName.setText(menuList.getText());
        return view;
    }
}
