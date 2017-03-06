package com.example.weina.bishe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.weina.bishe.R;
import com.example.weina.bishe.adapter.HomeAdapter;
import com.example.weina.bishe.entity.GoodsEntity;
import com.example.weina.bishe.service.IHomeService;
import com.example.weina.bishe.service.serviceImpl.BaseUserService;
import com.example.weina.bishe.service.serviceImpl.HomeService;
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
    private ArrayList<GoodsEntity> listData;
    private static Handler mhandler;
    private static int page;
    public static final String GOOD_BUNDLE = "good_bundle";
    //底部按钮
    private ImageButton mMeButton;
    private ImageButton mHomeButton;
    //
    private ImageButton mSearchButton;
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
        //初始化 页面
        page= 1;
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable(){
                    public void run() {
                        page =1;
                        HomeService.getContent(listData);
                    }

                });            //refresh data here
            }

            @Override
            public void onLoadMore() {
                new Handler().post(new Runnable(){
                    public void run() {
                        HomeService.getContent(listData);

                    }
                });

            }
        });

        listData = new ArrayList<>();
        mAdapter = new HomeAdapter(listData);
        //设置 监听 ****************************
        mAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(" 标记",""+position);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,GoodDetailActivity.class);
                intent.putExtra(GOOD_BUNDLE,listData.get(position-2));
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
                    case IHomeService.LOAD_OVER:{
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.loadMoreComplete();
                    }
                    case IHomeService.POSITION_MSG:{
                        mAdapter.notifyDataSetChanged();
                        break;
                    }
                }
                super.handleMessage(msg);
            }
        };
        mAdapter.setHandler(mhandler);
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
                if(null != BaseUserService.getInstatnce()) {
                    BaseUserService.getInstatnce().checkUser(MainActivity.this);
                }else {
                    Log.w("警告","空指针");
                }

            }
        });
        mSearchButton=(ImageButton)header.findViewById(R.id.home_search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    public static int getPage() {
        return page;
    }

    public static void setPage(int page) {
        MainActivity.page = page;
    }

    public static Handler getHandle(){
       return mhandler;
    }

}
