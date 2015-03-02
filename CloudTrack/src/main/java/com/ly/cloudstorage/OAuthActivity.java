package com.ly.cloudstorage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.ly.cloudstorage.global.Constants;
import com.vdisk.android.VDiskAuthSession;
import com.vdisk.android.VDiskDialogListener;
import com.vdisk.net.exception.VDiskDialogError;
import com.vdisk.net.exception.VDiskException;
import com.vdisk.net.session.AccessToken;
import com.vdisk.net.session.AppKeyPair;
import com.vdisk.net.session.Session;


public class OAuthActivity extends SherlockActivity implements VDiskDialogListener{
    private Button btn_oauth;
    private VDiskAuthSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppKeyPair appKeyPair = new AppKeyPair(Constants.CONSUMER_KEY,Constants.CONSUMER_SECRET);
        session = VDiskAuthSession.getInstance(this,appKeyPair, Session.AccessType.APP_FOLDER);
        setContentView(R.layout.activity_oauth);

        btn_oauth = (Button) findViewById(R.id.btnOAuth);
        btn_oauth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setRedirectUrl(Constants.REDIRECT_URL);
                session.authorize(OAuthActivity.this,OAuthActivity.this);
            }
        });

        //如果已经登录，直接跳转到测试页面
        if (session.isLinked()){
            AccessToken mToken = session.getAccessToken();
            Log.d("Login", "token-->" + mToken.mAccessToken
                    + "; expires time-->" + mToken.mExpiresIn + "; uid-->"
                    + mToken.mUid + "; refresh toke-->" + mToken.mRefreshToken);
            startActivity(new Intent(this,MainActivity.class));
            finish();

        }
    }


    /**
     * 认证结束后的回调方法
     * @param values
     */
    @Override
    public void onComplete(Bundle values) {
        if (values!=null){
            AccessToken mToken = (AccessToken) values.getSerializable(VDiskAuthSession.OAUTH2_TOKEN);
            Log.d("Login","token-->" + mToken.mAccessToken
                    + "; expires time-->" + mToken.mExpiresIn + "; uid-->"
                    + mToken.mUid + "; refresh toke-->" + mToken.mRefreshToken);
            session.finishAuthorize(mToken);
        }

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * 认证异常的回调方法
     * @param error
     */
    @Override
    public void onError(VDiskDialogError error) {
        Toast.makeText(getApplicationContext(),
                "Auth error : " + error.getMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * 认证异常的回调方法
     * @param exception
     */
    @Override
    public void onVDiskException(VDiskException exception) {
        Toast.makeText(getApplicationContext(),
                "Auth exception : " + exception.getMessage(), Toast.LENGTH_LONG)
                .show();
    }

    /**
     * 认证被取消的回调方法
     */
    @Override
    public void onCancel() {
        Toast.makeText(getApplicationContext(), "Auth cancel",
                Toast.LENGTH_LONG).show();
    }
}
