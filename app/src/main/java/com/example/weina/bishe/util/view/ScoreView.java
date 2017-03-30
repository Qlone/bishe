package com.example.weina.bishe.util.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.weina.bishe.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weina on 2017/3/28.
 */
public class ScoreView extends LinearLayout implements View.OnTouchListener{
    private int mScore;
    private List<ImageView> mImageScore;
    private ChangeCallBack mChangeCallBack;
    private boolean isTouchUse;
    public interface ChangeCallBack{
        void callBack();
    }

    public ScoreView(Context context,AttributeSet attrs) {
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.score_view, this);
        setOnTouchListener(this);
        initData();
        initView();
    }
    private void initData(){
        isTouchUse =true;
        mScore = 5;
        mImageScore = new ArrayList<>();
    }
    private void initView(){
        mImageScore.add((ImageView)findViewById(R.id.score1));
        mImageScore.add((ImageView)findViewById(R.id.score2));
        mImageScore.add((ImageView)findViewById(R.id.score3));
        mImageScore.add((ImageView)findViewById(R.id.score4));
        mImageScore.add((ImageView)findViewById(R.id.score5));
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(isTouchUse) {
            int length = (int) view.getWidth();
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    int now = (int) motionEvent.getX();
                    changeImage(now, length);
                    break;
                }
                case MotionEvent.ACTION_MOVE: {

                    changeImage((int) motionEvent.getX(), length);
                    break;
                }

            }
        }
        return true;
    }


    private void changeImage(int now,int length){
        Log.d(" 总长度  坐标",""+length+":"+now);
        if( (double)length/now >= 16 ){
            changeSrc(0,R.drawable.zero);
        }else if( (double)length/now > 5 ){
            changeSrc(1,R.drawable.one);
        }else if( (double)length/now > 2.5 ){
            changeSrc(2,R.drawable.two);
        }
        else if( (double)length/now > 1.66 ){
            changeSrc(3,R.drawable.three);
        }
        else if( (double)length/now > 1.25 ){
            changeSrc(4,R.drawable.four);
        }else if((double)length/now > 0){
            changeSrc(5,R.drawable.five);
        }
    }

    /**
     *
     * @param amount 改变多少个
     * @param src 改变成哪个资源
     */
    private void changeSrc(int amount,int src){
        for (int i = 0; i < mImageScore.size(); i++) {
            if (i < amount) {
                mImageScore.get(i).setImageResource(src);
            } else {
                mImageScore.get(i).setImageResource(R.drawable.nothing);
            }
        }
        mScore =amount;
        if(null != mChangeCallBack){
            mChangeCallBack.callBack();
        }
    }

    public int getScore() {
        return mScore;
    }

    public void setChangeCallBack(ChangeCallBack changeCallBack) {
        mChangeCallBack = changeCallBack;
    }

    public void setScoreInit(int score){
        mScore = score;
        changeImage(mScore*10+1,56);
    }
    public void setTouchUse(boolean isTouchUse){
        this.isTouchUse = isTouchUse;
    }
}
