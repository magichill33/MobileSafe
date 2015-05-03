package com.ly.yida.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ly.yida.MainActivity;
import com.ly.yida.R;
import com.ly.yida.SplashActivity;
import com.ly.yida.config.FragTag;

public class LoginFragment extends BaseFragment {

    private TextView tv_signup;
	@Override
	protected View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.frag_login,null);
        tv_signup = (TextView) view.findViewById(R.id.tv_signup);
		return view;
	}

	@Override
	protected void initData(Bundle savedInstanceState) {

	}

	@Override
	protected void registerEvent() {
        tv_signup.setOnClickListener(this);
	}

    @Override
    public void onClick(View view) {
        MainActivity activity = (MainActivity) getActivity();
        switch (view.getId()){
            case R.id.tv_signup:
                activity.switchFragment(FragTag.REGISTERTAG);
                break;
        }
    }
}
