package com.kch.www.newkchproject.DataSet;

/**
 * Created by YONSAI on 2017-05-12.
 */

public class UserlistDataset {

    int id;
    String name;
    String text;

    public UserlistDataset(int id, String name, String text) {
        this.id = id;
        this.name = name;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
