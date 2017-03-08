package com.example.weina.bishe.util.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.weina.bishe.R;
import com.example.weina.bishe.adapter.HomeAdapter;
import com.example.weina.bishe.controller.GoodDetailActivity;
import com.example.weina.bishe.controller.MainActivity;
import com.example.weina.bishe.controller.SearchActivity;
import com.example.weina.bishe.entity.GoodsEntity;
import com.example.weina.bishe.service.serviceImpl.HomeService;
import com.example.weina.bishe.util.SpacesItemDecoration;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

/**
 * Created by weina on 2017/3/8.
 */
public class HomeFragment extends Fragment {
    private XRecyclerView mRecyclerView;
    private HomeAdapter mAdapter;
    private ArrayList<GoodsEntity> listData;
    private static int page;
    private static Handler mhandler;
    private ImageButton mSearchButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, null);
        initView(view);
        return view;
    }

    private void initView(View view){
        //内容
        mRecyclerView = (XRecyclerView)view.findViewById(R.id.recyclerview);

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
        View header =   LayoutInflater.from(view.getContext()).inflate(R.layout.mian_content_head, (ViewGroup)view.findViewById(android.R.id.content),false);
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
                intent.setClass(getContext(),GoodDetailActivity.class);
                intent.putExtra(MainActivity.GOOD_BUNDLE,listData.get(position-2));
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);


        mhandler = MainActivity.getHandle();
        mAdapter.setHandler(mhandler);
        //第一次打开获取数据
        HomeService.getContent(listData);

        /**
         * 按钮  绑定
         */


        mSearchButton=(ImageButton)header.findViewById(R.id.home_search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    public static int getPage() {
        return page;
    }

    public static void setPage(int page) {
        HomeFragment.page = page;
    }

    public void updateOver(){
        mAdapter.notifyDataSetChanged();
        mRecyclerView.refreshComplete();
    }
    public void loadOver(){
        mAdapter.notifyDataSetChanged();
        mRecyclerView.loadMoreComplete();
    }
    public void positionMsg(){
        mAdapter.notifyDataSetChanged();
    }
}
