package com.example.weina.bishe.adapter;

import android.content.Context;
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
    public MenuAdapter(Context context, int textViewResourceId, List<MenuList> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
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
        //绑定数据
        fruitName.setText(menuList.getText());
        return view;
    }
}
