package com.kch.www.newkchproject.DataSet;

import android.support.v4.app.Fragment;

/**
 * Created by YONSAI on 2017-05-12.
 */

public class PagerDataSet {

    Fragment frag;
    String title;
    int id;

    public PagerDataSet(Fragment frag, String title, int id) {
        this.frag = frag;
        this.title = title;
        this.id = id;
    }

    public Fragment getFrag() {
        return frag;
    }

    public void setFrag(Fragment frag) {
        this.frag = frag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
