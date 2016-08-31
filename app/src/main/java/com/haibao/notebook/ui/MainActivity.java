package com.haibao.notebook.ui;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haibao.notebook.R;
import com.haibao.notebook.adapter.BaseRecyclerViewAdapter;
import com.haibao.notebook.adapter.NoteAdapter;
import com.haibao.notebook.constant.Constans;
import com.haibao.notebook.db.Dbhelper;
import com.haibao.notebook.entity.EventBusEntity;
import com.haibao.notebook.entity.NoteEntity;
import com.haibao.notebook.utils.SharePreferencesUtils;
import com.haibao.notebook.utils.SnackbarUtils;

import com.haibao.notebook.utils.ThemeUtils;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity {

    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;
    @ViewInject(R.id.dl_DrawerLayout)
    private DrawerLayout mDrawerLayout;
    @ViewInject(R.id.fl_center)
    private CoordinatorLayout mFrameLayout;
    @ViewInject(R.id.srl_Refresh)
    private SwipeRefreshLayout mRefresh;
    @ViewInject(R.id.rv_RecyclerView)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.fab_ActionButton)
    private FloatingActionButton mActionButton;
    @ViewInject(R.id.progress_wheel)
    private ProgressWheel mProgressWheel;
    @ViewInject(R.id.lv_listview)
    private ListView mListView;
    @ViewInject(R.id.bt_writer)
    private Button mWriter;
    @ViewInject(R.id.ll_left)
    private LinearLayout mLeftMenu;

    private NoteAdapter noteAdapter;

    private SearchView searchView;

    private List<String> menu_list;

    private ArrayAdapter<String> menu_adapter;

    private String currentType;//当前类型


    private List<NoteEntity> noteEntitys = new ArrayList<>();


    public void onEventMainThread(Integer integer) {
        switch (integer) {
            case Constans.CHANGE_DATA:
                mProgressWheel.stopSpinning();
                noteAdapter.setList(noteEntitys);
                break;
            case Constans.UPDATE:
                selectData(currentType);
                break;
            case Constans.CHANGE_THEME_BUS:
                this.recreate();
                break;
        }
    }

    public void onEvent(EventBusEntity ee){
        switch (ee.getFlag()){
            case Constans.CHANGEMENU:
                try {
                    String[] ss= (String[]) ee.getObject();
                    LogUtil.d(ss[1]+","+ss[0]);
                    Dbhelper.getDbManager().execNonQuery("update note set type='"+ss[1]+"' where type='"+ss[0]+"'");
                    Dbhelper.getDbManager().execNonQuery("update note set pretype='"+ss[1]+"' where pretype="+ss[1]+"'");
                } catch (DbException e) {
                    LogUtil.d(e.toString());
                    e.printStackTrace();
                }
                this.recreate();
                break;
            case Constans.ADDMENU:
                this.recreate();
                break;
            case Constans.DELETEMENU:
                try {
                    Dbhelper.getDbManager().execNonQuery("delete from note where type='"+((String)ee.getObject())+"' or pretype='"+((String)ee.getObject())+"'");
                } catch (DbException e) {
                    e.printStackTrace();
                }
                this.recreate();
                break;


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (mySharedPreferences.getBoolean(getString(R.string.right_hand_mode_key), false)) {
            setMenuListViewGravity(Gravity.END);
        } else {
            setMenuListViewGravity(Gravity.START);
        }
        if (mySharedPreferences.getBoolean(getString(R.string.card_note_item_layout_key), false)) {
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
    }

    /**
     * 設置開始和結束左右滑動的區別
     *
     * @param gravity
     */
    private void setMenuListViewGravity(int gravity) {
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mLeftMenu.getLayoutParams();
        params.gravity = gravity;
        mLeftMenu.setLayoutParams(params);
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        initToolbar(mToolbar);
        initDrawerLayout();
        initRecycleView();
        initWriterButton();
    }


    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openOrCloseDrawer();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void openOrCloseDrawer() {
        if (mDrawerLayout.isDrawerOpen(mLeftMenu)) {
            mDrawerLayout.closeDrawer(mLeftMenu);
        } else {
            mDrawerLayout.openDrawer(mLeftMenu);
        }
    }

    /**
     * 初始化菜单栏
     */
    public void initListView() {
        String menu_json = SharePreferencesUtils.getString(MainActivity.this, Constans.MENU_LIST_KEY, "null");
        Gson gson = new Gson();
        if ("null".equals(menu_json)) {
            menu_list = Arrays.asList(getResources().getStringArray(R.array.array_menu));
            SharePreferencesUtils.putString(this, Constans.MENU_LIST_KEY, gson.toJson(menu_list));
        }else {
            menu_list=gson.fromJson(menu_json,new TypeToken<List<String>>(){}.getType());
        }
        currentType = menu_list.get(0);
        mToolbar.setTitle(currentType);
        menu_adapter = new ArrayAdapter<String>(this, R.layout.item_menu, menu_list);
        mListView.setAdapter(menu_adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentType = menu_list.get(i);
                mToolbar.setTitle(currentType);
                selectData(currentType);
                openOrCloseDrawer();
            }
        });


        mWriter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,EditMenuListActivity.class);
                startActivity(intent);
            }
        });
    }

    private ActionBarDrawerToggle mDrawerToggle;

    /**
     * 初始化滑动菜单
     */
    public void initDrawerLayout() {
        initListView();
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.syncState();
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setScrimColor(getResources().getColor(R.color.drawer_scrim_color));
    }

    /**
     * 初始化按钮
     */
    public void initWriterButton() {
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteEntity note = new NoteEntity();
                note.setType(currentType);
                startNoteActivity(Constans.NEW_NOTE_EDIT, note);
            }
        });
    }

    private void startNoteActivity(int type, NoteEntity note) {
        Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Constans.EDITKEY, type);
        EventBus.getDefault().postSticky(note);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    /**
     * 初始化内容
     */
    public void initRecycleView() {
        mRefresh.setColorSchemeColors(ThemeUtils.getColorPrimary(this));
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SnackbarUtils.showSnackbar(mRefresh, "请设置同步账号");
                        mRefresh.setRefreshing(false);
                    }
                }, 1000);
            }
        });
        noteAdapter = new NoteAdapter(noteEntitys);
        mRecyclerView.setAdapter(noteAdapter);
        noteAdapter.setOnInViewClickListener(R.id.ib_button, new BaseRecyclerViewAdapter.onInternalClickListenerImpl<NoteEntity>() {
            @Override
            public void OnClickListener(View parentV, View v, Integer position, NoteEntity values) {
                showPopupMenu(v, values);
            }
        });
        noteAdapter.setOnInViewClickListener(R.id.notes_item_root, new BaseRecyclerViewAdapter.onInternalClickListenerImpl<NoteEntity>() {

            @Override
            public void OnClickListener(View parentV, View v, Integer position, NoteEntity values) {
                super.OnClickListener(parentV, v, position, values);
                startNoteActivity(Constans.LOOK_NOTE_EDIT, values);
            }
        });
        selectData(currentType);
    }

    /**
     * 查询数据
     */
    public void selectData(final String type) {
        mProgressWheel.spin();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List list = Dbhelper.getDbManager().selector(NoteEntity.class).where("type", "=", type).findAll();
                    if (list == null) list = new ArrayList();
                    noteEntitys.clear();
                    noteEntitys.addAll(list);
                    EventBus.getDefault().post(Constans.CHANGE_DATA);
                } catch (DbException e) {
                    e.printStackTrace();
                    mProgressWheel.stopSpinning();
                }
            }
        }).start();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.sync:
                SnackbarUtils.showSnackbar(mRefresh, "敬请期待");
                return true;
            case R.id.about:
                SnackbarUtils.showSnackbar(mRefresh, "敬请期待");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        ComponentName componentName = getComponentName();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        searchView.setQueryHint(getString(R.string.search_note));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                noteAdapter.getFilter().filter(s);
                return true;
            }
        });
        return true;
    }


    public void showPopupMenu(View view, final NoteEntity noteEntity) {
        PopupMenu popupMenu = new PopupMenu(this, view, R.attr.popupMenuStyle);
        if ("回收站".equals(currentType)) {
            popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "还原");
        } else {
            popupMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, "编辑");
            popupMenu.getMenu().add(Menu.NONE, 3, Menu.NONE, "移至回收站");
        }
        popupMenu.getMenu().add(Menu.NONE, 4, Menu.NONE, "永久删除");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1://还原
                        try {
                            Dbhelper.getDbManager().execNonQuery("update note set type=pretype,pretype='' where noteid=" + noteEntity.getNoteID());
                            EventBus.getDefault().post(Constans.UPDATE);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2://编辑
                        startNoteActivity(Constans.CHANGE_NOTE_EDIT, noteEntity);
                        break;
                    case 3://移至回收站
                        try {
                            Dbhelper.getDbManager().execNonQuery("update note set pretype=type,type='回收站' where noteid=" + noteEntity.getNoteID());
                            EventBus.getDefault().post(Constans.UPDATE);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 4://彻底删除
                        try {
                            Dbhelper.getDbManager().execNonQuery("delete from note where noteid=" + noteEntity.getNoteID());
                            EventBus.getDefault().post(Constans.UPDATE);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return false;
            }
        });

    }
}
