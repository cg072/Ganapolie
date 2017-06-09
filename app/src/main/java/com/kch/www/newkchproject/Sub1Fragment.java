package com.kch.www.newkchproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by YONSAI on 2017-05-12.
 */

public class Sub1Fragment extends Fragment implements View.OnClickListener{

    Button btn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_sub_1,container,false);

        btn = (Button)rootView.findViewById(R.id.s1Btn);

        btn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        SubActivity activity = (SubActivity)getActivity();
        activity.btnClick(2);
    }
}
