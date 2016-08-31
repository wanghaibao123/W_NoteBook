package com.haibao.notebook.ui;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haibao.notebook.R;
import com.haibao.notebook.constant.Constans;
import com.haibao.notebook.entity.EventBusEntity;
import com.haibao.notebook.utils.AlertDialogUtils;
import com.haibao.notebook.utils.SharePreferencesUtils;
import com.haibao.notebook.utils.SnackbarUtils;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by whb on 2016/8/30.
 * Email 18720982457@163.com
 */
public class EditMenuListActivity extends BaseActivity {
    @ViewInject(R.id.toolbar)
    private Toolbar mToobar;
    @ViewInject(R.id.sv_list_menu)
    private LinearLayout linearLayout;

    private String menulist;
    private List<String> list;

    @Override
    protected int getLayoutView() {
        return R.layout.activity_editmenu;
    }


    public void onEvent(int i) {
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        initToolbar(mToobar);
        initMenuListView();
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        toolbar.setTitle("编辑菜单");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            final EditText editText = new EditText(this);
            AlertDialogUtils.chanMenuName(EditMenuListActivity.this, editText, new DialogInterface.OnClickListener[]{null, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int j) {
                    if (list.contains(editText.getText().toString())) {
                        SnackbarUtils.showSnackbar(mToobar, "对不起改名称已存在");
                    } else {
                        Gson gson = new Gson();
                        LinearLayout lintext = (LinearLayout) getLayoutInflater().inflate(R.layout.item_edittext, null);
                        final TextView tv = (TextView) lintext.findViewById(R.id.tv_text);
                        tv.setText(editText.getText().toString());
                        linearLayout.addView(lintext);
                        list.add(list.size() - 1, editText.getText().toString());
                        SharePreferencesUtils.putString(EditMenuListActivity.this, Constans.MENU_LIST_KEY, gson.toJson(list));
                        EventBusEntity eventBusEntity = new EventBusEntity();
                        eventBusEntity.setFlag(Constans.ADDMENU);
                        EventBus.getDefault().post(eventBusEntity);
                    }
                }
            }});
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void initMenuListView() {
        menulist = SharePreferencesUtils.getString(EditMenuListActivity.this, Constans.MENU_LIST_KEY);
        final Gson gson = new Gson();
        list = gson.fromJson(menulist, new TypeToken<List<String>>() {
        }.getType());
        LinearLayout editText;
        for (int i = 0; i < list.size() - 1; i++) {
            final EventBusEntity eventBusEntity = new EventBusEntity();
            editText = (LinearLayout) getLayoutInflater().inflate(R.layout.item_edittext, null);
            final TextView tv = (TextView) editText.findViewById(R.id.tv_text);
            final String name = list.get(i);
            tv.setText(name);
            linearLayout.addView(editText);
            final AlertDialog[] alertDialog = new AlertDialog[1];
            final int finalI = i;
            final LinearLayout finalEditText = editText;
            editText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    alertDialog[0] = AlertDialogUtils.showSelectDialog(EditMenuListActivity.this, new String[]{"修改菜单", "删除菜单"}, new View.OnClickListener[]{new View.OnClickListener() {
                        @Override
                        public void onClick(final View view) {
                            alertDialog[0].cancel();
                            final EditText et = new EditText(EditMenuListActivity.this);
                            et.setText(name);
                            et.setSelection(name.length());
                            AlertDialogUtils.chanMenuName(EditMenuListActivity.this, et, new DialogInterface.OnClickListener[]{null, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int j) {
                                    if (list.contains(et.getText().toString())) {
                                        SnackbarUtils.showSnackbar(mToobar, "对不起改名称已存在");
                                    } else {
                                        eventBusEntity.setFlag(Constans.CHANGEMENU);
                                        eventBusEntity.setObject(new String[]{list.get(finalI), et.getText().toString()});
                                        EventBus.getDefault().post(eventBusEntity);
                                        tv.setText(et.getText().toString());
                                        list.remove(finalI);
                                        list.add(finalI, et.getText().toString());
                                        SharePreferencesUtils.putString(EditMenuListActivity.this, Constans.MENU_LIST_KEY, gson.toJson(list));
                                    }
                                }
                            }});
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            eventBusEntity.setFlag(Constans.DELETEMENU);
                            eventBusEntity.setObject(list.get(finalI));
                            EventBus.getDefault().post(eventBusEntity);
                            list.remove(finalI);
                            SharePreferencesUtils.putString(EditMenuListActivity.this, Constans.MENU_LIST_KEY, gson.toJson(list));
                            alertDialog[0].cancel();
                            linearLayout.removeView(finalEditText);

                        }
                    }});
                    return false;
                }
            });
        }
    }
}
