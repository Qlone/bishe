package com.example.weina.bishe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.example.weina.bishe.R;
import com.example.weina.bishe.adapter.CommentAdater;
import com.example.weina.bishe.entity.CommentEntity;
import com.example.weina.bishe.service.serviceImpl.CommentService;
import com.example.weina.bishe.util.SpacesItemDecoration;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

/**
 * Created by weina on 2017/3/29.
 */
public class CommentListActivity extends AppCompatActivity {
    public static final String COMMENTLIS_BUNDLE = "commentList_bundle";
    public static final int HANLE_UPDATA_OVER = 100;
    public static final int HANLE_LOAD_OVER = 101;
    private XRecyclerView mXRecyclerView;
    private ArrayList<CommentEntity> mCommentEntities;
    private CommentAdater mCommentAdater;
    private static int page=1;
    private static int lines=8;
    //handle
    private static Handler sHandler;
    private int goodsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.commentList_toolbar);
        setSupportActionBar(toolbar);
        SysApplication.getInstance().addActivity(this);
        initData();
        initView();
    }

    private void initData(){
        Intent intent = getIntent();
        goodsId = intent.getIntExtra( COMMENTLIS_BUNDLE,0);
        mCommentEntities = new ArrayList<>();
        mCommentAdater = new CommentAdater(mCommentEntities);
        sHandler = new Handler(){
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case HANLE_UPDATA_OVER:{
                        updataOver();
                        break;
                    }
                    case HANLE_LOAD_OVER:{
                        loadOver();
                        break;
                    }
                }
            }
        };
    }
    private void initView(){
        /**
         * recycleview 初始化
         */

        mXRecyclerView = (XRecyclerView) findViewById(R.id.comment_list_recycler);
        mXRecyclerView.setLayoutManager( new LinearLayoutManager(this));
        /**
         * 设置分割线
         */
        SpacesItemDecoration decoration=new SpacesItemDecoration(8);
        mXRecyclerView.addItemDecoration(decoration);
        mXRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mXRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.Pacman);

        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page=1;
                CommentService.getCommentList(goodsId,mCommentEntities);
            }

            @Override
            public void onLoadMore() {
                CommentService.getCommentList(goodsId,mCommentEntities);
            }
        });
        mXRecyclerView.setAdapter(mCommentAdater);
        page=1;
        CommentService.getCommentList(goodsId,mCommentEntities);
    }

    public static int getPage() {
        return page;
    }

    public static void setPage(int pages) {
        page = pages;
    }

    public static int getLines() {
        return lines;
    }

    public static void setLines(int lines) {
        CommentListActivity.lines = lines;
    }
    private void updataOver(){
        mCommentAdater.notifyDataSetChanged();
        mXRecyclerView.refreshComplete();
    }
    private void loadOver(){
        mCommentAdater.notifyDataSetChanged();
        mXRecyclerView.loadMoreComplete();
    }

    public static Handler getHandler() {
        return sHandler;
    }
}
