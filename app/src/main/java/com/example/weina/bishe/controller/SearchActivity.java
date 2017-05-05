package com.example.weina.bishe.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.weina.bishe.R;
import com.example.weina.bishe.adapter.HomeAdapter;
import com.example.weina.bishe.bean.GsonSortApply;
import com.example.weina.bishe.entity.GoodsEntity;
import com.example.weina.bishe.entity.LableEntity;
import com.example.weina.bishe.service.ISearchService;
import com.example.weina.bishe.service.serviceImpl.GoodsService;
import com.example.weina.bishe.service.serviceImpl.SearchService;
import com.example.weina.bishe.util.SpacesItemDecoration;
import com.example.weina.bishe.util.view.SearchView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by weina on 2017/3/3.
 */
public class SearchActivity extends AppCompatActivity implements SearchView.SearchViewListener{
    public static final String LABLE_BUNDL = "lable_bundle";
    public static final String Title_BUNDL = "title_bundle";
    private List<String> lableList;
    /**
     * 搜索结果列表view
     */
    private XRecyclerView lvResults;

    private HomeAdapter mAdapter;
    private ArrayList<GoodsEntity> listData;

    /**
     * 搜索view
     */
    private SearchView searchView;


    /**
     * 热搜框列表adapter
     */
    private ArrayAdapter<String> hintAdapter;

    /**
     * 自动补全列表adapter
     */
    private ArrayAdapter<String> autoCompleteAdapter;

    /**
     * 热搜版数据
     */
    private List<String> hintData;
    /**
     * 搜索过程中自动补全数据
     */
    private List<String> autoCompleteData;

    /**
     * 默认提示框显示项的个数
     */
    public static int DEFAULT_HINT_SIZE = 6;

    /**
     * 提示框显示项的个数
     */
    private static int hintSize = DEFAULT_HINT_SIZE;
    /**
     * 下拉 选项框
     */
    private ListView mListView;

    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Handler mHandler;
    private static int page;
    //搜索
    private GsonSortApply mGsonSortApply;
    /**
     * 设置提示框显示项的个数
     *
     * @param hintSize 提示框显示个数
     */
    public static void setHintSize(int hintSize) {
        SearchActivity.hintSize = hintSize;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        SysApplication.getInstance().addActivity(this);
        initData();
        initViews();
    }

    @Override
    protected void  onDestroy(){
        super.onDestroy();
    }
    @Override
    public void onBackPressed(){
        if(mListView.getVisibility()!= View.GONE){
            mListView.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }
    /**
     * 初始化数据
     */
    private void initData() {


        //初始化热搜版数据
        getHintData();
        //初始化自动补全数据
        getAutoCompleteData(null);
        //获取第一次数据
        mGsonSortApply =  new GsonSortApply();
        lableList = (List<String>)getIntent().getStringArrayListExtra(LABLE_BUNDL);
        String title = getIntent().getStringExtra(Title_BUNDL);
        mGsonSortApply.setTitle(null==title  ? "":title);
    }
    /**
     * 初始化视图
     */
    private void initViews() {
        mListView = (ListView) findViewById(R.id.search_lv_tips);
        lvResults = (XRecyclerView) findViewById(R.id.search_list);
        searchView = (SearchView) findViewById(R.id.main_search_layout);
        //设置监听
        searchView.setSearchViewListener(this);
        searchView.setXRecyclerView(lvResults);
        //设置adapter
        searchView.setTipsHintAdapter(hintAdapter);
        searchView.setAutoCompleteAdapter(autoCompleteAdapter);
        if(null!=lableList){
            for(String name:lableList){
                searchView.addMark(name);
            }
        }
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
//        staggeredGridLayoutManager.setAutoMeasureEnabled(true);
        lvResults.setLayoutManager(staggeredGridLayoutManager);
//        mRecyclerView.setHasFixedSize(true);
        /**
         * 设置分割线
         */
        SpacesItemDecoration decoration=new SpacesItemDecoration(8);
        lvResults.addItemDecoration(decoration);
        lvResults.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        lvResults.setLaodingMoreProgressStyle(ProgressStyle.Pacman);
        page= 1;
        lvResults.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page=1;
                getData();
            }

            @Override
            public void onLoadMore() {
                getData();
            }
        });
        listData = new ArrayList<>();
        mAdapter = new HomeAdapter(listData);
        lvResults.setAdapter(mAdapter);
        mHandler = new Handler(){
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ISearchService.UPDATE_OVER: {
                        mAdapter.notifyDataSetChanged();
                        lvResults.refreshComplete();
                        break;
                    }
                    case ISearchService.LOAD_OVER:{
                        mAdapter.notifyDataSetChanged();
                        lvResults.loadMoreComplete();
                    }
                    case ISearchService.POSITION_MSG:{
                        mAdapter.notifyDataSetChanged();
                        break;
                    }
                }
                super.handleMessage(msg);
            }
        };
        mAdapter.setHandler(mHandler);
        getData();


        mAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(" 标记",""+position);
                Intent intent = new Intent();
                intent.setClass(SearchActivity.this,GoodDetailActivity.class);
                intent.putExtra(MainActivity.GOOD_BUNDLE,listData.get(position-1));
                startActivity(intent);
            }
        });
    }

    /**
     * 获取热搜版data 和adapter
     */
    private void getHintData() {
        if(null == hintData) {
            hintData = new ArrayList<>(hintSize);
        }else {
            hintData.clear();
        }
        hintAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hintData);
        SearchService.getHotLable(new SearchService.HintDataCallback() {
                @Override
                public void callback(List<LableEntity> lableList) {
                    for(LableEntity list:lableList){
                        hintData.add(""+ list.getText());
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            hintAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
    }
    /**
     * 获取自动补全data 和adapter
     */
    private void getAutoCompleteData(String text) {
        if (autoCompleteData == null) {
            //初始化
            autoCompleteData = new ArrayList<>(hintSize);
        } else {
            // 根据text 获取auto data
            autoCompleteData.clear();
            SearchService.getHintLable(text, new SearchService.HintDataCallback() {
                @Override
                public void callback(List<LableEntity> lableList) {
                    List<String> res = new ArrayList<String>();
                    for(LableEntity list:lableList){
                        autoCompleteData.add(list.getText());
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (null == autoCompleteAdapter) {
                                autoCompleteAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_list_item_1, autoCompleteData);
                            } else {
                                autoCompleteAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            });
        }
        if (null == autoCompleteAdapter) {
            autoCompleteAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_list_item_1, autoCompleteData);
        } else {
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }





    @Override
    public void onRefreshAutoComplete(String text) {
    //更新数据
        getAutoCompleteData(text);
    }

    @Override
    public void onSearch(String text) {
        SearchService.searchLable(text);
        page=1;
        mGsonSortApply.setTitle(text);
        //重置 view 使得重新搜索后可以加载更多
        recyclViewReset();
        getData();


    }

    private void recyclViewReset(){
        try {
            //反射改变变量
            Field field = lvResults.getClass().getDeclaredField("isnomore");
            Field field2 = lvResults.getClass().getDeclaredField("previousTotal");
            Field field3 = lvResults.getClass().getDeclaredField("mLastY");
            field.setAccessible(true);
            field2.setAccessible(true);
            field3.setAccessible(true);


            field.set(lvResults,false);
            field2.set(lvResults,0);
            field3.set(lvResults,-1);

        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
    private void getData(){
        mGsonSortApply.setSort(searchView.getSort());
        mGsonSortApply.setType(searchView.getType());
        mGsonSortApply.setLable(searchView.getLable());
        GoodsService.highSearch(listData,mGsonSortApply);
    }

    public static int getPage() {
        return page;
    }

    public static void setPage(int page) {
        SearchActivity.page = page;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static void setHandler(Handler handler) {
        mHandler = handler;
    }

    public static void openSearch(Context mContext,String title,ArrayList<String> lableList){
        Intent intent = new Intent();
        intent.setClass(mContext,SearchActivity.class);
        intent.putStringArrayListExtra(LABLE_BUNDL,lableList);
        intent.putExtra(Title_BUNDL,title);
        mContext.startActivity(intent);
    }
}
