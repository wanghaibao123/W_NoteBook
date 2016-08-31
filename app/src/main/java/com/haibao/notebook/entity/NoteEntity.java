package com.haibao.notebook.entity;

import org.xutils.db.annotation.Column;


import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by whb on 2016/8/21.
 * Email 18720982457@163.com
 */
@Table(name="note")
public class NoteEntity {
    @Column(name="noteid",isId = true)
    private int noteID;
    @Column(name="type")
    private String type;
    @Column(name="titlename")
    private String titleName;
    @Column(name="content")
    private String content;
    @Column(name="time")
    private String time;
    @Column(name="pretype")
    private String pretype;

    public int getNoteID() {
        return noteID;
    }

    public void setNoteID(int noteID) {
        this.noteID = noteID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPretype() {
        return pretype;
    }

    public void setPretype(String pretype) {
        this.pretype = pretype;
    }

    @Override
    public String toString() {
        return "NoteEntity{" +
                "noteID=" + noteID +
                ", type='" + type + '\'' +
                ", titleName='" + titleName + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
