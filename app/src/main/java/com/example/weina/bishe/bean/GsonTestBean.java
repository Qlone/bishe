
package com.example.weina.bishe.bean;

import java.util.List;

/**
 * Created by weina on 2017/2/11.
 */
public class GsonTestBean {
    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * text : context
         */

        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
