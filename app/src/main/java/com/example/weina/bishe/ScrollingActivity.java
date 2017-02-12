package com.example.weina.bishe;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.weina.bishe.adapter.HomeAdapter;
import com.example.weina.bishe.service.HomeService;
import com.example.weina.bishe.util.SpacesItemDecoration;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

public class ScrollingActivity extends AppCompatActivity {
    private XRecyclerView mRecyclerView;
    private HomeAdapter mAdapter;
    private ArrayList<String> listData;
    private int refreshTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //不要标题
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbarLayout.setTitle(" ");


        mRecyclerView = (XRecyclerView)this.findViewById(R.id.recyclerview);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//
//        mRecyclerView.setLayoutManager(layoutManager);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
//        staggeredGridLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
//        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
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
//        View header =   LayoutInflater.from(this).inflate(R.layout.recyclerview_header, (ViewGroup)findViewById(android.R.id.content),false);
//        mRecyclerView.addHeaderView(header);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime ++;
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        listData.clear();
                        for(int i = 0; i < 15 ;i++){
                            listData.add("item" + i + "after " + refreshTime + " times of refresh");
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

        listData = new  ArrayList<String>();
        mAdapter = new HomeAdapter(listData);
        for(int i = 0; i < 15 ;i++){
            listData.add("item" + i);
        }
        mRecyclerView.setAdapter(mAdapter);
        /**
         * 浮动框
         */
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
