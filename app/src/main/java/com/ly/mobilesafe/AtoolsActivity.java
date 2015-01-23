package com.ly.mobilesafe;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.ly.mobilesafe.utils.SmsUtils;

public class AtoolsActivity extends Activity {

    private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}

	/**
	 * 
	 * @param view
	 */
	public void numberQuery(View view)
	{
		Intent intent = new Intent(this, NumberAddressQueryActivity.class);
		startActivity(intent);
	}

    /**
     * 点击事件，短信备份
     * @param view
     */
    public void smsBackup(View view)
    {
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在备份短信");
        pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SmsUtils.backupSms(getApplicationContext(),new SmsUtils.BackUpCallBack() {
                        @Override
                        public void beforeBackup(int max) {
                            pd.setMax(max);
                        }

                        @Override
                        public void onSmsBackup(int process) {
                            pd.setProgress(process);
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AtoolsActivity.this,"备份成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AtoolsActivity.this,"备份失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }finally {
                    pd.dismiss();
                }
            }
        }).start();
    }

    public void smsRestore(View view)
    {
        SmsUtils.restoreSms(this, false);
        Toast.makeText(this, "还原成功",Toast.LENGTH_SHORT).show();
    }

}
