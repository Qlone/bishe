package com.example.weina.bishe.controller;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weina.bishe.R;
import com.example.weina.bishe.adapter.HomeAdapter;
import com.example.weina.bishe.service.HomeService;
import com.example.weina.bishe.util.SpacesItemDecoration;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

/**
 * Created by weina on 2017/2/12.
 */
public class MainActivity extends AppCompatActivity {
    private XRecyclerView mRecyclerView;
    private HomeAdapter mAdapter;
    private ArrayList<String> listData;
    private int refreshTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        // configure the SlidingMenu
        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        menu.setShadowWidthRes(R.dimen.shadow_width);
//        menu.setShadowDrawable(R.drawable.shadow);
//
//        // 设置滑动菜单视图的宽度
       menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        menu.setMenu(R.layout.nav_header_main);

        //内容
        mRecyclerView = (XRecyclerView)this.findViewById(R.id.recyclerview);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
//        staggeredGridLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
//        mRecyclerView.setHasFixedSize(true);
        /**
         * 设置分割线
         */
        SpacesItemDecoration decoration=new SpacesItemDecoration(13);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.Pacman);
        /**
         * 添加 recyclerview头
         */
//        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        View header =   LayoutInflater.from(this).inflate(R.layout.mian_content_head, (ViewGroup)findViewById(android.R.id.content),false);
        mRecyclerView.addHeaderView(header);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime ++;
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        listData.clear();
                        for(int i = 0; i < 15 ;i++){
                            listData.add("item" + i + "after " + refreshTime + " times of refreshhahahahahahahahahahahahahahahahahahahahahahahaha");
                        }
                        HomeService.getContent();
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.refreshComplete();
                    }

                }, 3000);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        for(int i = 0; i < 15 ;i++){
                            listData.add("item" + (i + listData.size()) );
                        }
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.loadMoreComplete();
                    }
                }, 3000);

            }
        });

        listData = new ArrayList<String>();
        mAdapter = new HomeAdapter(listData);
        for(int i = 0; i < 15 ;i++){
            listData.add("item" + i);
        }
        mRecyclerView.setAdapter(mAdapter);
    }

}
