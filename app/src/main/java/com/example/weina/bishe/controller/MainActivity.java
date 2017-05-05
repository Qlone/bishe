package com.example.weina.bishe.controller;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.weina.bishe.R;
import com.example.weina.bishe.adapter.MenuAdapter;
import com.example.weina.bishe.bean.MenuList;
import com.example.weina.bishe.service.IHomeService;
import com.example.weina.bishe.service.serviceImpl.BaseUserService;
import com.example.weina.bishe.util.view.HomeFragment;
import com.example.weina.bishe.util.view.HomeFragment2;
import com.example.weina.bishe.util.view.HomeFragment3;
import com.example.weina.bishe.util.view.RegisterDialog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weina on 2017/2/12.
 */
public class MainActivity extends AppCompatActivity {

    private boolean isBack;
    private ArrayList<MenuList> mMenuLists;
    private static Handler mhandler;
    public static final String GOOD_BUNDLE = "good_bundle";
    //底部按钮
    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;

    //定义一个布局
    private LayoutInflater layoutInflater;

    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {HomeFragment.class,HomeFragment2.class,HomeFragment3.class};

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.home_button1,R.drawable.cart_button1,R.drawable.me_button1};
    //Tab选项卡的文字
    private String mTextviewArray[] = {"0", "1", "2"};
    //其他属性
    private TextView mUserName;
    private List<ImageView> imageList = new ArrayList<ImageView>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        SysApplication.getInstance().addActivity(this);
        /**
         * 初始化 菜单内容
         */
        initData();
        initView();
        Log.d("mainActivity","onCreate");

    }
    @Override
    public void onBackPressed() {
            if(isBack){
                super.onBackPressed();
                SysApplication.getInstance().exit();
            }else{
                isBack =true;
                Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            isBack = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
    }

    private void initData(){
        isBack =false;//点击两次返回
        mMenuLists = new ArrayList<>();
        mMenuLists.add(new MenuList("地址管理",R.drawable.menu_address));
        mMenuLists.add(new MenuList("订单管理",R.drawable.menu_order));
        mMenuLists.add(new MenuList("注销",R.drawable.menu_out));
        mMenuLists.add(new MenuList("注册",R.drawable.menu_register));
        mhandler = new Handler(){
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case IHomeService.UPDATE_OVER: {
                        updateOver();
                        break;
                    }
                    case IHomeService.LOAD_OVER:{
                        loadOver();
                        break;
                    }
                    case IHomeService.POSITION_MSG:{
                        positonMes();
                        break;
                    }
                    case IHomeService.ORDER_UPDATA_OVER:{
                        OrderUpdateOver();
                        break;
                    }
                    case IHomeService.HOST_TAB_ID1:{
                        setTabHostView(IHomeService.HOST_TAB_ID1);
                        break;
                    }
                    case IHomeService.ORDER_UPDATA:{
                        int position = msg.getData().getInt(IHomeService.POSITION_FLAG);
                        orderUpdata(position);
                        break;
                    }
                    case IHomeService.CHANG_USER_NAME:{
                        setUserName();
                        break;
                    }
                }
                super.handleMessage(msg);
            }
        };
    }
    private void initView(){
        //初始化其他
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

        mUserName = (TextView) findViewById(R.id.header_text_userName);
        //设置监听
        mUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseUserService.getInstatnce().checkUser(MainActivity.this,null);
            }
        });
        listView.setAdapter(menuAdapter);
        /**
         * 菜单管理
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 1:{//
                        if(BaseUserService.getInstatnce().checkUser(MainActivity.this,null)) {
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, OrderMgActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }
                    case 0:{
                        if(BaseUserService.getInstatnce().checkUser(MainActivity.this,null)) {
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, AddressMgActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }
                    case 2:{
                        if(null != BaseUserService.getGsonLogin() &&  BaseUserService.getGsonLogin().isBoolean()){
                            BaseUserService.logout();
                            Toast.makeText(MainActivity.this,"注销成功",Toast.LENGTH_SHORT).show();
                            setTabHostView(IHomeService.HOST_TAB_ID1);
                        }else {
                            Toast.makeText(MainActivity.this,"没有登录",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }case 3:{
                        RegisterDialog registerDialog = new RegisterDialog(MainActivity.this);
                        registerDialog.show();
                    }
                }
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

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
// 设置所有选项卡的图片为未选中图片
                imageList.get(0).setBackgroundResource(R.color.white);
                imageList.get(1).setBackgroundResource(R.color.white);
                imageList.get(2).setBackgroundResource(R.color.white);

                if (tabId.equalsIgnoreCase("0")) {
                    imageList.get(0).setBackgroundResource(R.color.colorPrimaryDark);
                    // 移动底部背景图片
                    //moveTopSelect(0);
                } else if (tabId.equalsIgnoreCase("1")) {
                    imageList.get(1).setBackgroundResource(R.color.colorPrimaryDark);
                    // 移动底部背景图片
                    //moveTopSelect(1);
                } else if (tabId.equalsIgnoreCase("2")) {
                    imageList.get(2).setBackgroundResource(R.color.colorPrimaryDark);
                    // 移动底部背景图片
                    //moveTopSelect(2);
                }
            }
        });
        mTabHost.setCurrentTab(0);
        imageList.get(0).setBackgroundResource(R.color.colorPrimaryDark);

    }

    //获取 菜单tab
    private View getTabItemView(int index){
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.home_fragment_button);
        imageList.add(imageView);
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
    public void OrderUpdateOver(){
        String tag= mTabHost.getCurrentTabTag();
        try {
            HomeFragment2 fragment = (HomeFragment2) getSupportFragmentManager().findFragmentByTag(tag);
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
    public void orderUpdata(int positon){
        String tag= mTabHost.getCurrentTabTag();
        mTabHost.getTabContentView();
        try {
            HomeFragment2 fragment = (HomeFragment2) getSupportFragmentManager().findFragmentByTag(tag);
            fragment.updataData(positon);
        }catch (Exception e){
            Log.d("当前页面不是 主页","");
            e.printStackTrace();
        }
    }
    public void setTabHostView(int i){
        mTabHost.setCurrentTab(i);
    }


    //设置标题
    public void setUserName(){
        mhandler.post(new Runnable() {
            @Override
            public void run() {
                if(BaseUserService.getGsonLogin().isBoolean()){
                    mUserName.setText("您好,"+BaseUserService.getGsonLogin().getUserEntity().getUserName());
                }else {
                    mUserName.setText("请登录");
                }
            }
        });
    }
}
