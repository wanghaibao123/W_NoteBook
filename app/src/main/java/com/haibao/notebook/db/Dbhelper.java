package com.haibao.notebook.db;

import android.os.Environment;

import org.xutils.DbManager;
import org.xutils.common.util.FileUtil;
import org.xutils.x;

import java.io.File;

/**
 * Created by whb on 2016/8/21.
 * Email 18720982457@163.com
 */
public class Dbhelper {
    private static DbManager dbManager;
    private static DbManager.DaoConfig daoConfig;

    static {
        daoConfig = new DbManager.DaoConfig()
                .setDbName("note.db")
            //    .setDbDir(new File("/sdcard"))
                .setDbVersion(1)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging();//提升写速度
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        //TODO
                    }
                });
    }

    public static DbManager getDbManager() {
        if (dbManager == null) {
            dbManager = x.getDb(daoConfig);
        }
        return dbManager;
    }
}