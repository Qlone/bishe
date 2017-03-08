package com.example.weina.bishe.controller;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;

import com.example.weina.bishe.R;
import com.example.weina.bishe.adapter.MenuAdapter;
import com.example.weina.bishe.bean.MenuList;
import com.example.weina.bishe.service.IHomeService;
import com.example.weina.bishe.util.view.HomeFragment;
import com.example.weina.bishe.util.view.HomeFragment2;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

/**
 * Created by weina on 2017/2/12.
 */
public class MainActivity extends AppCompatActivity {
    private ArrayList<MenuList> mMenuLists;
    private static Handler mhandler;
    public static final String GOOD_BUNDLE = "good_bundle";
    //底部按钮
    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;

    //定义一个布局
    private LayoutInflater layoutInflater;

    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {HomeFragment.class,HomeFragment2.class,HomeFragment2.class};

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.home_button,R.drawable.cart_button,R.drawable.me_button};
    //Tab选项卡的文字
    private String mTextviewArray[] = {"0", "1", "2"};

    //主页面
    private HomeFragment mHomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        /**
         * 初始化 菜单内容
         */
        initData();
        initView();

    }

    private void initData(){
        mMenuLists = new ArrayList<>();
        mMenuLists.add(new MenuList("hello",0));
        mhandler = new Handler(){
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case IHomeService.UPDATE_OVER: {
                        updateOver();
                        break;
                    }
                    case IHomeService.LOAD_OVER:{
                        loadOver();
                    }
                    case IHomeService.POSITION_MSG:{
                        positonMes();
                        break;
                    }
                }
                super.handleMessage(msg);
            }
        };
    }
    private void initView(){
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

        MenuAdapter menuAdapter = new MenuAdapter(MainActivity.this,R.layout.main_menu_list,mMenuLists);
        ListView listView = (ListView) findViewById(R.id.menu_list);
        listView.setAdapter(menuAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        /**
         * tabhost
         */
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        //得到fragment的个数
        int count = fragmentArray.length;

        for(int i = 0; i < count; i++){
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            //mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }
    }

    //获取 菜单tab
    private View getTabItemView(int index){
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.home_fragment_button);
        imageView.setImageResource(mImageViewArray[index]);


        return view;
    }

    public static int getPage() {
        return HomeFragment.getPage();
    }

    public static void setPage(int page) {
        HomeFragment.setPage(page);
    }

    public static Handler getHandle(){
       return mhandler;
    }

    public void updateOver(){
        String tag= mTabHost.getCurrentTabTag();
        try {
            HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(tag);
            fragment.updateOver();
        }catch (Exception e){
            Log.d("当前页面不是 主页","");
            e.printStackTrace();
        }
    }
    public void loadOver(){
        String tag= mTabHost.getCurrentTabTag();
        try {
            HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(tag);
            fragment.loadOver();
        }catch (Exception e){
            Log.d("当前页面不是 主页","");
            e.printStackTrace();
        }
    }
    public void positonMes(){
        String tag= mTabHost.getCurrentTabTag();
        mTabHost.getTabContentView();
        try {
            HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(tag);
            fragment.positionMsg();
        }catch (Exception e){
            Log.d("当前页面不是 主页","");
            e.printStackTrace();
        }
    }

}
