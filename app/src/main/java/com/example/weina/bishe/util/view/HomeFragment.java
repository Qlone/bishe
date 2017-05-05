package com.example.weina.bishe.util.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private ImageButton mSearchButton;
    private ArrayList<Button> mButtons;
    //缓存自身
    private static View mView;
    private Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();
        if(null == mView) {
            mView = inflater.inflate(R.layout.main_fragment, null);
            initView(mView);

        }
        //        //第一次打开获取数据
        HomeService.getContent(listData);
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误
        ViewGroup viewGroup = (ViewGroup) mView.getParent();
        if(null != viewGroup){
            viewGroup.removeView(viewGroup);
        }
        return  mView;
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mContext =context;
    }
    private void initData(){
        //初始化 页面
        page= 1;

        if(null == listData) {
            listData = new ArrayList<>();
        }else {
            listData.clear();
            mAdapter.notifyDataSetChanged();
        }
        if(null == mAdapter) {
            mAdapter = new HomeAdapter(listData);
            //设置 监听 ****************************
            mAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Log.d(" 标记",""+position);
                    Intent intent = new Intent();
                    intent.setClass(mContext,GoodDetailActivity.class);
                    intent.putExtra(MainActivity.GOOD_BUNDLE,listData.get(position-2));
                    startActivity(intent);
                }
            });
        }
        if(null == mButtons){
            mButtons = new ArrayList<>();
        }
    }

    private void initView(View view){
        Log.d("初始化 "," recycle");
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
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                MainActivity.getHandle().post(new Runnable(){
                    public void run() {
                        page =1;
                        HomeService.getContent(listData);
                    }

                });            //refresh data here
            }

            @Override
            public void onLoadMore() {
                MainActivity.getHandle().post(new Runnable(){
                    public void run() {
                        HomeService.getContent(listData);

                    }
                });

            }
        });
        //初始化 页面
        page= 1;

        if(null == listData) {
            listData = new ArrayList<>();
        }else {
            listData.clear();
            mAdapter.notifyDataSetChanged();
        }
        if(null == mAdapter) {
            mAdapter = new HomeAdapter(listData);
            //设置 监听 ****************************
            mAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Log.d(" 标记",""+position);
                    Intent intent = new Intent();
                    intent.setClass(mContext,GoodDetailActivity.class);
                    intent.putExtra(MainActivity.GOOD_BUNDLE,listData.get(position-2));
                    startActivity(intent);
                }
            });
        }

        mRecyclerView.setAdapter(mAdapter);

        /**
         * 按钮  绑定
         */


        mSearchButton=(ImageButton)header.findViewById(R.id.home_search_button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,SearchActivity.class);
                startActivity(intent);
            }
        });
        initButton(header);
    }

    private void initButton(View view){
        mButtons.add((Button)view.findViewById(R.id.home_head_button_1));
        mButtons.add((Button)view.findViewById(R.id.home_head_button_2));
        mButtons.add((Button)view.findViewById(R.id.home_head_button_3));
        mButtons.add((Button)view.findViewById(R.id.home_head_button_4));
        mButtons.add((Button)view.findViewById(R.id.home_head_button_5));
        mButtons.add((Button)view.findViewById(R.id.home_head_button_6));
        for(final Button button:mButtons){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<String> lable = new ArrayList<String>();
                    lable.add(button.getText().toString());
                    SearchActivity.openSearch(view.getContext(),"",lable);
                }
            });
        }
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
