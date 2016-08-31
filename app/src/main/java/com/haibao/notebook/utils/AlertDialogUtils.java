package com.haibao.notebook.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haibao.notebook.R;

import org.xutils.common.util.DensityUtil;

import java.util.List;

/**
 * Created by whb on 2016/8/31.
 * Email 18720982457@163.com
 */
public class AlertDialogUtils {

    public static AlertDialog showSelectDialog(Context context, String[] bts, View.OnClickListener[] listeners){
        AlertDialog alertDialog=new AlertDialog.Builder(context).create();
        LayoutInflater layoutInflater= LayoutInflater.from(context);
        LinearLayout view=null;
        view= (LinearLayout) layoutInflater.inflate(R.layout.item_select,null);
        TextView textView;
        for(int i=0;i<bts.length;i++){
            textView= (TextView) layoutInflater.inflate(R.layout.item_select_item,null);
            textView.setText(bts[i]);
            textView.setOnClickListener(listeners[i]);
            view.addView(textView);
        }
        alertDialog.setView(view,0,20,0,20);
        alertDialog.show();
        return alertDialog;
    }

    public static void chanMenuName(Context context,EditText et, DialogInterface.OnClickListener[] OnClickListeners){
        AlertDialog alertDialog=new AlertDialog.Builder(context).create();
        alertDialog.setTitle("修改名称");
        alertDialog.setView(et,30,10,30,0);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"确定", OnClickListeners[1]);
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"取消", OnClickListeners[0]);
        alertDialog.show();
    }

    private static void expandDialogWidth(AlertDialog dialog,int width) {
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width =  DensityUtil.dip2px(width);
        dialog.getWindow().setAttributes(lp);
    }

}
