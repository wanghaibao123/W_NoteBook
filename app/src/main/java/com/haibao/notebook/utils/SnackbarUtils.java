package com.haibao.notebook.utils;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.haibao.notebook.R;

/**
 * Created by whb on 2016/8/28.
 * Email 18720982457@163.com
 */
public class SnackbarUtils {
    /**
     * 展示Snackbar 控制时间
     * @param view
     * @param charSequence
     * @param during
     */
    public static void showSnackbar(View view,CharSequence charSequence,int during){
        Snackbar sb=Snackbar.make(view,charSequence,during);
        sb.getView().setBackgroundColor(ThemeUtils.getColorPrimary(view.getContext()));
        sb.show();
    }

    /**
     * 展示Snackbar 固定时间
     * @param view
     * @param charSequence
     */
    public static void showSnackbar(View view,CharSequence charSequence){
        showSnackbar(view,charSequence,Snackbar.LENGTH_LONG);
    }

    /**
     * 展示snackbar 并且添加点击事件
     * @param view
     * @param charSequence
     * @param charSequence_2
     * @param onClickListener
     */
    public static void clickSnackBar(View view, CharSequence charSequence, CharSequence charSequence_2, View.OnClickListener onClickListener){
        Snackbar sb=Snackbar.make(view,charSequence,Snackbar.LENGTH_LONG);
        sb.getView().setBackgroundColor(ThemeUtils.getColorPrimary(view.getContext()));
        sb.setAction(charSequence_2,onClickListener);
        sb.show();
    }
}
