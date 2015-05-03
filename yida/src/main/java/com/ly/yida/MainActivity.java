package com.ly.yida;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ly.yida.config.FragTag;
import com.ly.yida.fragment.BaseFragment;
import com.ly.yida.fragment.LoginFragment;
import com.ly.yida.fragment.RegisterFragment;

public class MainActivity extends BaseActivity {
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();
        switchFragment(loginFragment);
	}


    public void switchFragment(BaseFragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content,fragment);
        transaction.commit();
    }

    public void switchFragment(String tag){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content,getFragmentByTag(tag),tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    protected BaseFragment getFragmentByTag(String tag)
    {
        BaseFragment fragment = null;
        if (FragTag.LOGINTAG.equals(tag))
        {
            fragment = loginFragment;
        }else if(FragTag.REGISTERTAG.equals(tag))
        {
            fragment = registerFragment;
        }

        return fragment;
    }
}
