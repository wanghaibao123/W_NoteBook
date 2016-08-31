package com.haibao.notebook.ui;


import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.haibao.notebook.R;
import com.haibao.notebook.constant.Constans;
import com.haibao.notebook.db.Dbhelper;
import com.haibao.notebook.entity.NoteEntity;
import com.haibao.notebook.utils.TimeUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by whb on 2016/8/21.
 * Email 18720982457@163.com
 */
public class EditNoteActivity extends BaseActivity {
    @ViewInject(R.id.toolbar)
    private Toolbar mToobar;
    @ViewInject(R.id.tv_edittitle)
    private MaterialEditText edittitle;
    @ViewInject(R.id.content_edit_text)
    private MaterialEditText contenttext;
    @ViewInject(R.id.tv_time)
    private TextView time;

    private MenuItem saveitem;

    private NoteEntity note;

    int intExtra;

    @Override
    protected int getLayoutView() {
        return R.layout.activity_editnote;
    }

    @Override
    protected void initView() {
        initToolbar(mToobar);
        EventBus.getDefault().registerSticky(this);//粘性注册
        edittitle.addTextChangedListener(new SimpleTextWatcher());
        contenttext.addTextChangedListener(new SimpleTextWatcher());
    }

    public void onEvent(NoteEntity note) {
        this.note = note;
        edittitle.setText(note.getTitleName());
        contenttext.setText(note.getContent());
        if (intExtra == Constans.LOOK_NOTE_EDIT) {
            edittitle.clearFocus();
            contenttext.clearFocus();
        } else {
            edittitle.requestFocus();
            edittitle.setSelection(edittitle.length());
            contenttext.setSelection(contenttext.length());
        }
        time.setText(TimeUtils.getTime(note.getTime() == null ? TimeUtils.getCurrentTimeInLong() : Long.parseLong(note.getTime())));
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        super.initToolbar(toolbar);
        intExtra = getIntent().getIntExtra(Constans.EDITKEY, 0);
        switch (intExtra) {
            case Constans.CHANGE_NOTE_EDIT:
                mToobar.setTitle("编辑");
                break;
            case Constans.LOOK_NOTE_EDIT:
                mToobar.setTitle("查看");
                break;
            case Constans.NEW_NOTE_EDIT:
                mToobar.setTitle("新建");
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        saveitem = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                try {
                    saveNote();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveNote() throws DbException {
        DbManager db = Dbhelper.getDbManager();
        note.setTitleName(edittitle.getText().toString());
        note.setContent(contenttext.getText().toString());
        note.setTime(TimeUtils.getCurrentTimeInLong() + "");
        if (note.getNoteID() == 0) {
            db.save(note);
        } else {
            db.update(note);
        }
        EventBus.getDefault().post(Constans.UPDATE);
        this.finish();
    }

    class SimpleTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (!TextUtils.isEmpty(edittitle.getText().toString()) && !TextUtils.isEmpty(contenttext.getText().toString())) {
                saveitem.setVisible(true);
            } else {
                saveitem.setVisible(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
