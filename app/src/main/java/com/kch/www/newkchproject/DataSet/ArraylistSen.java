package com.kch.www.newkchproject.DataSet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by YONSAI on 2017-05-17.
 */

public class ArraylistSen implements Serializable{

    ArrayList<Map<String,Object>> addrData;

    public ArraylistSen(ArrayList<Map<String, Object>> addrData) {
        this.addrData = addrData;
    }

    public ArrayList<Map<String, Object>> getAddrData() {
        return addrData;
    }

    public void setAddrData(ArrayList<Map<String, Object>> addrData) {
        this.addrData = addrData;
    }
}
