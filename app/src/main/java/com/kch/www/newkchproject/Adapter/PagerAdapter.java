package com.kch.www.newkchproject.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kch.www.newkchproject.DataSet.PagerDataSet;

import java.util.ArrayList;

/**
 * Created by YONSAI on 2017-05-12.
 */

public class PagerAdapter extends FragmentStatePagerAdapter{

    ArrayList<PagerDataSet> data;

    public PagerAdapter(FragmentManager fm, ArrayList<PagerDataSet> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position).getFrag();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return data.get(position).getTitle();
    }
}
