package com.haibao.notebook.ui;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.haibao.notebook.R;
import com.haibao.notebook.adapter.ThemeAdapter;
import com.haibao.notebook.constant.Constans;
import com.haibao.notebook.utils.ThemeUtils;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by whb on 2016/8/26.
 * Email 18720982457@163.com
 */
public class SettingActivity extends BaseActivity{
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;
    @Override
    protected int getLayoutView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        initToolbar(mToolbar);
        getPreferences(0);
        getFragmentManager().beginTransaction()
                .add(R.id.fl_center, new SettingFragemnt())
                .commit();
    }
    public void onEvent(int i){}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbar.setTitle("设置");
    }


    public  static  class SettingFragemnt extends PreferenceFragment{
        private final String SETTING="setting";
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
            if(getString(R.string.change_theme).equals(preference.getKey())){
                LinearLayout linearLayout= (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.alertdialog,null);
                GridView gridView= (GridView) linearLayout.findViewById(R.id.gd_Theme);
                final Integer[] integers={R.drawable.indigo_round,R.drawable.cyan_round,R.drawable.green_round,R.drawable.grey_round,R.drawable.orange_round,R.drawable.purple_round,R.drawable.red_round,R.drawable.yellow_round};
                ThemeAdapter themeAdapter=new ThemeAdapter(getActivity(), Arrays.asList(integers));
                gridView.setAdapter(themeAdapter);
                final AlertDialog alertDialog=new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("请选择主题");
                alertDialog.setView(linearLayout,50,20,50,40);
                alertDialog.show();
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ThemeUtils.saveTheme(view.getContext(),integers[i]);
                        EventBus.getDefault().post(Constans.CHANGE_THEME_BUS);
                        ((SettingActivity)getActivity()).reload(false);
                        alertDialog.cancel();
                    }
                });

            }
            return super.onPreferenceTreeClick(preferenceScreen, preference);

        }
    }
}
