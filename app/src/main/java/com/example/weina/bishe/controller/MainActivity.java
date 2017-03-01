package com.example.weina.bishe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.weina.bishe.R;
import com.example.weina.bishe.adapter.HomeAdapter;
import com.example.weina.bishe.bean.GoodsRoughBean;
import com.example.weina.bishe.service.IHomeService;
import com.example.weina.bishe.service.serviceImpl.HomeService;
import com.example.weina.bishe.util.SpacesItemDecoration;
import com.example.weina.bishe.util.view.LoginDialog;
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
    private ArrayList<GoodsRoughBean.GoodsBean> listData;
    private static Handler mhandler;

    //底部按钮
    private ImageButton mMeButton;
    private ImageButton mHomeButton;
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
        SpacesItemDecoration decoration=new SpacesItemDecoration(8);
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
                new Handler().post(new Runnable(){
                    public void run() {
                        HomeService.getContent(listData);
                    }

                });            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.loadMoreComplete();
                    }
                }, 3000);

            }
        });

        listData = new ArrayList<>();
        mAdapter = new HomeAdapter(listData);
        //设置 监听 ****************************
        mAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this,GoodDetailActivity.class);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);


        mhandler = new Handler(){
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case IHomeService.UPDATE_OVER: {
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.refreshComplete();
                        break;
                    }
                    case IHomeService.POSITION_MSG:{
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.refreshComplete();
                        break;
                    }
                }
                super.handleMessage(msg);
            }
        };
        //第一次打开获取数据
        HomeService.getContent(listData);
        //底部按钮
        mMeButton = (ImageButton)findViewById(R.id.home_me_button);
        mMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ScrollingActivity.class);
                startActivity(intent);
            }
        });
        mHomeButton = (ImageButton)findViewById(R.id.home_home_button);
        mHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginDialog loginDialog = new LoginDialog(MainActivity.this);
                loginDialog.show();
            }
        });

    }

    public static Handler getHandle(){
        return mhandler;
    }

}
