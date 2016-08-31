package com.haibao.notebook.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.haibao.notebook.R;
import com.haibao.notebook.utils.ThemeUtils;

import java.util.List;

/**
 * Created by whb on 2016/8/29.
 * Email 18720982457@163.com
 */
public class ThemeAdapter extends BaseAdapter{
    private Context context;
    private List<Integer> list;
    LayoutInflater layoutInflater;
    private int currentTheme;

    public ThemeAdapter(Context context,List<Integer> list){
        this.context=context;
        this.list=list;
       layoutInflater=LayoutInflater.from(context);
        currentTheme= ThemeUtils.getCurrentTheme(context);
    }
    @Override
    public int getCount() {
        return list==null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView= (ImageView) layoutInflater.inflate(R.layout.item_theme,viewGroup,false);
        imageView.setBackground(ContextCompat.getDrawable(context,list.get(i)));
        if(list.get(i)!=currentTheme){
            imageView.setImageResource(0);
        }
        return imageView;
    }
}
