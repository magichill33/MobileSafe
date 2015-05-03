package com.ly.yida;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.ly.yida.fragment.BaseFragment;
import com.ly.yida.fragment.LoginFragment;

public class MainActivity extends FragmentActivity {
    private LoginFragment loginFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        loginFragment = new LoginFragment();
        switchFragment(loginFragment);
	}


    protected void switchFragment(BaseFragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content,fragment).commit();
    }
}
