package com.lilosoft.xtcm.module;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.base.NormalBaseActivity;
import com.lilosoft.xtcm.constant.Config;
import com.lilosoft.xtcm.database.SharedPreferencesFactory;
import com.lilosoft.xtcm.views.TitleBar;
import com.lilosoft.xtcm.views.TitleBar.STYLE;

public class SetupActivity extends NormalBaseActivity implements
        OnClickListener {

    TitleBar mTitleBar;
    EditText setup_ip;
    EditText setup_point;
    Button setup_submit;
    Button setup_exit;

    @Override
    protected void installViews() {
        // TODO Auto-generated method stub

        setContentView(R.layout.activity_setup);
        initTitleBar();
        initView();
        initValues();
    }

    /**
     * @category 初始化titleBar
     */
    protected void initTitleBar() {

        mTitleBar = (TitleBar) findViewById(R.id.titlebar);

        mTitleBar.changeStyle(STYLE.NOT_BTN_AND_TITLE);

        mTitleBar.centerTextView.setText(R.string.function_setup);

    }

    private void initView() {
        setup_ip = (EditText) findViewById(R.id.setup_ip);
        setup_point = (EditText) findViewById(R.id.setup_point);
        setup_submit = (Button) findViewById(R.id.setup_submit);
        setup_exit = (Button) findViewById(R.id.setup_exit);
    }

    public void initValues() {
        String defultUrl = new SharedPreferencesFactory().getConfig(mContext);
        if (null != defultUrl) {
            String strs[] = defultUrl.split(",");
            setup_ip.setText(strs[0] + "");
            setup_point.setText(strs[1] + "");
        } else {
            defultUrl = Config.getURL_();
            defultUrl = defultUrl.substring(defultUrl.indexOf("//"));

            setup_ip.setText(defultUrl.substring(2, defultUrl.indexOf(":"))
                    + "");
            setup_point.setText(defultUrl.substring(defultUrl.indexOf(":") + 1,
                    defultUrl.lastIndexOf("/")) + "");

            new SharedPreferencesFactory().changeIpConfig(
                    mContext,
                    defultUrl.substring(2, defultUrl.indexOf(":")),
                    defultUrl.substring(defultUrl.indexOf(":") + 1,
                            defultUrl.lastIndexOf("/")));

        }

    }

    @Override
    protected void registerEvents() {
        // TODO Auto-generated method stub
        setup_submit.setOnClickListener(this);
        setup_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.setup_submit:
                new SharedPreferencesFactory().changeIpConfig(mContext, setup_ip
                        .getText().toString(), setup_point.getText().toString());
                Toast.makeText(mContext, "IP修改成功！", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.setup_exit:
                finish();
                break;

            default:
                break;
        }
    }

}
