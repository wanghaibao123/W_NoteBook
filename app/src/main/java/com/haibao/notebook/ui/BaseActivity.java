package com.haibao.notebook.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.haibao.notebook.R;
import com.haibao.notebook.utils.ThemeUtils;

import org.xutils.x;

/**
 * Created by zhulong-book on 2016/7/21.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public final static String IS_START_ANIM = "IS_START_ANIM";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTheme();
        setContentView(getLayoutView());
        x.view().inject(this);
        initView();
    }

    private void initTheme(){
        int theme= ThemeUtils.getCurrentTheme(this);
        ThemeUtils.changeTheme(this, theme);
    }


    protected void initToolbar(Toolbar toolbar) {
        if (toolbar == null)
            return;
        toolbar.setTitle(R.string.app_name);
        toolbar.collapseActionView();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void reload(boolean anim) {
        Intent intent = getIntent();
        if (!anim) {
            overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra(BaseActivity.IS_START_ANIM, false);
        }
        finish();
        if (!anim) {
            overridePendingTransition(0, 0);
        }
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    protected abstract int getLayoutView();

    protected abstract void initView();

}
