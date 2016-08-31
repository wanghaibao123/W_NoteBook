package com.haibao.notebook.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import com.haibao.notebook.R;
import com.haibao.notebook.constant.Constans;
import com.haibao.notebook.ui.BaseActivity;

/**
 * Created by whb on 2016/8/30.
 * Email 18720982457@163.com
 */
public class ThemeUtils {
    public static int getColorPrimary(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    public static void saveTheme(Context context,int theme){
        SharePreferencesUtils.putInt(context, Constans.CHANGE_THEME,theme);
    }

    public static int getCurrentTheme(Context context){
        return SharePreferencesUtils.getInt(context,Constans.CHANGE_THEME,R.drawable.red_round);
    }

    public static void changeTheme(BaseActivity baseActivity, int theme) {
        switch (theme){
            case R.drawable.cyan_round:
                baseActivity.setTheme(R.style.cyanTheme);
                break;
            case R.drawable.green_round:
                baseActivity.setTheme(R.style.greenTheme);
                break;
            case R.drawable.grey_round:
                baseActivity.setTheme(R.style.greyTheme);
                break;
            case R.drawable.indigo_round:
                baseActivity.setTheme(R.style.indigoTheme);
                break;
            case R.drawable.orange_round:
                baseActivity.setTheme(R.style.orangeTheme);
                break;
            case R.drawable.purple_round:
                baseActivity.setTheme(R.style.purpleTheme);
                break;
            case R.drawable.red_round:
                baseActivity.setTheme(R.style.RedTheme);
                break;
            case R.drawable.yellow_round:
                baseActivity.setTheme(R.style.yellowTheme);
                break;
        }
    }
}
