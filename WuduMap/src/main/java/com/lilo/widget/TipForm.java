package com.lilo.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.lilo.sm.LiloMapActivity;
import com.lilo.sm.R;

public class TipForm {

    private static TipForm tipForm = new TipForm();
    private ProgressDialog progressDialog;

    /**
     * @param message
     *            弹出的提示信息
     *
     */
    public static void showToast(String message,Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT)
                .show();
    }

    public static TipForm newInstance()
    {
        return tipForm;
    }
    /**
     *  弹出进度条dialog
     */
    public void showProgressDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(context, "",context.getString(R.string.querying),
                    true);
            progressDialog.getWindow().setLayout(600, 300);
        } else {
            progressDialog.show();
        }
    }

    /**
     * 关闭进度条
     */
    public void dismissDialog()
    {
        if(progressDialog!=null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
