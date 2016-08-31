package com.haibao.notebook;

import android.app.Application;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

/**
 * Created by zhulong-book on 2016/7/21.
 */
public class NoteBookApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);

    }
}
