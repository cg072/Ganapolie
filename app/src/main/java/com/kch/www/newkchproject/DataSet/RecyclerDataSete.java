package com.kch.www.newkchproject.DataSet;

import android.graphics.Bitmap;

/**
 * Created by YONSAI on 2017-05-25.
 */

public class RecyclerDataSete {

    Bitmap id;
    String text;
    int prog;

    public RecyclerDataSete(Bitmap id, String text, int prog) {
        this.id = id;
        this.text = text;
        this.prog = prog;
    }

    public Bitmap getId() {
        return id;
    }

    public void setId(Bitmap id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getProg() {
        return prog;
    }

    public void setProg(int prog) {
        this.prog = prog;
    }
}
