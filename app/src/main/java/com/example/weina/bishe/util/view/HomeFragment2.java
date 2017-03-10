package com.example.weina.bishe.util.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weina.bishe.R;

/**
 * Created by weina on 2017/3/8.
 */
public class HomeFragment2 extends Fragment {
    private View mView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(null == mView) {
            mView = inflater.inflate(R.layout.main_fragment2, null);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误
        ViewGroup viewGroup = (ViewGroup) mView.getParent();
        if(null != viewGroup){
            viewGroup.removeView(viewGroup);
        }
        return  mView;

    }
}
