package com.example.weina.bishe.bean;

/**
 * Created by weina on 2017/3/7.
 */
public class MenuList {
    private String text;
    private int drawAble;

    public MenuList(String text, int drawAble) {
        this.text = text;
        this.drawAble = drawAble;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDrawAble() {
        return drawAble;
    }

    public void setDrawAble(int drawAble) {
        this.drawAble = drawAble;
    }
}
