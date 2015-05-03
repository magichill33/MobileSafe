package com.ly.yida.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.ly.yida.R;

/**
 * Created by magichill33 on 2015/5/3.
 */
public class RegisterFragment extends BaseFragment {

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.frag_register,null);
        return view;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void registerEvent() {

    }
}
