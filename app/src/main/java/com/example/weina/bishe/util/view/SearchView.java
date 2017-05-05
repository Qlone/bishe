package com.example.weina.bishe.util.view;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.weina.bishe.R;
import com.example.weina.bishe.service.serviceImpl.GoodsService;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by weina on 2017/3/3.
 */
public class SearchView extends LinearLayout implements View.OnClickListener {

    /**
     * 输入框
     */
    private EditText etInput;

    /**
     * 删除键
     */
    private ImageView ivDelete;

    /**
     * 返回按钮
     */
    private Button btnBack;

    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 弹出列表
     */
    private ListView lvTips;

    /**
     * 提示adapter （推荐adapter）
     */
    private ArrayAdapter<String> mHintAdapter;

    /**
     * 自动补全adapter 只显示名字
     */
    private ArrayAdapter<String> mAutoCompleteAdapter;

    /**
     * 搜索回调接口
     */
    private SearchViewListener mListener;

    //绑定按钮
    private EditText mMarkEdit;
    private Button mAddMark;
    private LinearLayout mMarkLinear;
    private RelativeLayout mDetailLayout;
    //绑定下拉框内容
    private Spinner mType;
    private Spinner mSort;
    private List<String> dataType;
    private List<String> dataSort;
    private ArrayAdapter<String> typeAdapter;
    private ArrayAdapter<String> sortAdapter;
    //其他属性
    private static Handler sHandle;
    private TranslateAnimation mShowAction;
    private TranslateAnimation mHiddenAction;
    private TranslateAnimation mRecyclAction;
    //其他
    private XRecyclerView mXRecyclerView;
    private Map<String,String> lableMap;
    /**
     * 设置搜索回调接口
     *
     * @param listener 监听者
     */
    public void setSearchViewListener(SearchViewListener listener) {
        mListener = listener;
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.search_layout, this);
        sHandle = new Handler();
        initDate();
        initViews();
    }
    private void initDate(){
        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -0.5f);
        mHiddenAction.setDuration(500);
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        lableMap = new HashMap<>();
    }

    private void initViews() {
        etInput = (EditText) findViewById(R.id.search_et_input);
        ivDelete = (ImageView) findViewById(R.id.search_iv_delete);
        btnBack = (Button) findViewById(R.id.search_btn_back);
        lvTips = (ListView) findViewById(R.id.search_lv_tips);
        //详细筛选
        mDetailLayout = (RelativeLayout) findViewById(R.id.search_detail);
        mDetailLayout.setVisibility(View.GONE);
        mMarkEdit = (EditText) findViewById(R.id.search_mark_eidtText);
        mAddMark = (Button) findViewById(R.id.search_mark_btn);
        mAddMark.setOnClickListener(this);
        //mark显示列表
        mMarkLinear = (LinearLayout) findViewById(R.id.search_scroll_linear);


        lvTips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //set edit text
                String text = lvTips.getAdapter().getItem(i).toString();
                etInput.setText(text);
                etInput.setSelection(text.length());
                //hint list view gone and result list view show
                lvTips.setVisibility(View.GONE);
                notifyStartSearching(text);
            }
        });

        ivDelete.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        etInput.addTextChangedListener(new EditChangedListener());
        etInput.setOnClickListener(this);
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    lvTips.setVisibility(GONE);
                    notifyStartSearching(etInput.getText().toString());
                }
                return true;
            }
        });
        initSort();
        mRecyclAction = new TranslateAnimation(0,0,0,-mDetailLayout.getHeight());
        mRecyclAction.setDuration(500);
    }
    //初始化排序
    private void initSort(){
        mSort = (Spinner) findViewById(R.id.search_spinner_sort);
        mType = (Spinner) findViewById(R.id.search_spinner_type);
        dataSort=  new ArrayList<>();
        dataType = new ArrayList<>();
        sortAdapter = new  ArrayAdapter<String>(mContext, R.layout.myspinner, dataSort);
        typeAdapter = new  ArrayAdapter<String>(mContext, R.layout.myspinner, dataType);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSort.setAdapter(sortAdapter);
        mType.setAdapter(typeAdapter);
        //绑定完毕
        GoodsService.getSort(dataType, dataSort, new GoodsService.GoodsCallBack() {
            @Override
            public void onSuccess(String data) {
                sHandle.post(new Runnable() {
                    @Override
                    public void run() {
                        sortAdapter.notifyDataSetChanged();
                        typeAdapter.notifyDataSetChanged();
                        Log.d("sortUpdata","succes datasort:"+dataSort.size()+"  datatyoe"+dataType.size());
                    }
                });
            }

            @Override
            public void onError() {
                Log.d("sortUpdata","fail");
            }
        });
    }

    /**
     * 通知监听者 进行搜索操作
     * @param text
     */
    private void notifyStartSearching(String text){
        if (mListener != null) {
            mListener.onSearch(text);
        }
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 设置热搜版提示 adapter
     */
    public void setTipsHintAdapter(ArrayAdapter<String> adapter) {
        this.mHintAdapter = adapter;
        if (lvTips.getAdapter() == null) {
            lvTips.setAdapter(mHintAdapter);
        }
    }

    /**
     * 设置自动补全adapter
     */
    public void setAutoCompleteAdapter(ArrayAdapter<String> adapter) {
        this.mAutoCompleteAdapter = adapter;
    }

    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!"".equals(charSequence.toString())) {
                ivDelete.setVisibility(VISIBLE);
                lvTips.setVisibility(VISIBLE);
                if (null != mAutoCompleteAdapter && mAutoCompleteAdapter != lvTips.getAdapter()) {
                    lvTips.setAdapter(mAutoCompleteAdapter);
                }
                //更新autoComplete数据
                if (null != mListener) {
                    mListener.onRefreshAutoComplete(charSequence + "");
                }
            } else {
                ivDelete.setVisibility(GONE);
                if (null != mHintAdapter) {
                    lvTips.setAdapter(mHintAdapter);
                }
                lvTips.setVisibility(GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_et_input:
                lvTips.setVisibility(VISIBLE);
                break;
            case R.id.search_iv_delete:
                etInput.setText("");
                ivDelete.setVisibility(GONE);
                break;
            case R.id.search_btn_back:
                lvTips.setVisibility(View.GONE);
                if(mDetailLayout.getVisibility() == View.GONE){
                    btnBack.setText("搜索");
                    mDetailLayout.startAnimation(mShowAction);
                    mDetailLayout.setVisibility(View.VISIBLE);
                }else {//此时是搜索按钮
                    btnBack.setText("展开");
                    notifyStartSearching(etInput.getText().toString());
//                    if(null != mXRecyclerView){
//                        mXRecyclerView.startAnimation(mRecyclAction);
//                        Log.d("recycle","move");
//                    }
                    mDetailLayout.startAnimation(mHiddenAction);
                    mDetailLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.search_mark_btn:
                String msg = mMarkEdit.getText().toString();
                if(null!=msg&&!"".equals(msg)){
                    addMark(msg);
                }
                break;
        }
    }

    /**
     * search view回调方法
     */
    public interface SearchViewListener {

        /**
         * 更新自动补全内容
         *
         * @param text 传入补全后的文本
         */
        void onRefreshAutoComplete(String text);

        /**
         * 开始搜索
         *
         * @param text 传入输入框的文本
         */
        void onSearch(String text);

//        /**
//         * 提示列表项点击时回调方法 (提示/自动补全)
//         */
//        void onTipsItemClick(String text);
    }

    public void addMark(String name){
        final View view = View.inflate(mContext, R.layout.layout_mark, null);
        TextView tv = (TextView) view.findViewById(R.id.textView1);
        tv.setText(name);
        String key = UUID.randomUUID().toString();
        Log.d("key",key);
        view.setTag(key);//第一个就是0
        Log.d("addView","名字"+name);
        ImageView iv = (ImageView) view.findViewById(R.id.search_mark_delete);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View views) {
                lableMap.remove((String)view.getTag());
                mMarkLinear.removeView(view);
            }
        });
        mMarkLinear.addView(view);
        lableMap.put(key,name);
    }

    public String getSort(){
        if(null == mSort){
            return "";
        }else {
            try {
                return mSort.getSelectedItem().toString();
            }catch (Exception e){
                return "";
            }
        }
    }
    public String getType(){
        if(null == mType){
            return "";
        }else {
            try {
                return mType.getSelectedItem().toString();
            }catch (Exception e){
                return "";
            }
        }
    }

    public void setXRecyclerView(XRecyclerView XRecyclerView) {
        mXRecyclerView = XRecyclerView;
    }

    public List<String> getLable() {
        List<String> res = new ArrayList<>();
        for(Map.Entry<String,String> e : lableMap.entrySet()){
            res.add(e.getValue());
        }
        return res;
    }

    public void setLableMap(Map<String, String> lableMap) {
        this.lableMap = lableMap;
    }
}