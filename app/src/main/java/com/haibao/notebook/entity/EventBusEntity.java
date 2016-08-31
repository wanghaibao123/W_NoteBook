package com.haibao.notebook.entity;

import java.util.Objects;

/**
 * Created by whb on 2016/8/31.
 * Email 18720982457@163.com
 */
public class EventBusEntity {
    private int flag;
    private Object object;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
