package com.lilosoft.xtcm.base;

import android.view.KeyEvent;
import android.view.View;

import com.lilosoft.xtcm.R;
import com.lilosoft.xtcm.utils.ActivityManager;
import com.lilosoft.xtcm.utils.LogFactory;

public abstract class NormalBaseActivity extends AbsBaseActivity {

    /**
     * @category 线程变量
     */
    protected Thread threadG = null;

    /**
     * @category 显示等待框
     *
     * @param tipMsg
     *            等待框消息
     */
    protected void showProgressDialog(int tipMsg) {
        showProgressDialog(mContext.getResources().getString(tipMsg));
    }

    /**
     * @category 显示等待框 * @param tipMsg 等待框消息
     */
    protected void showProgressDialog(String tipMsg) {
        progressDialogIsShow = true;
        mPProgressBar.setTextTipMsg(tipMsg);
        mPProgressBar.setTextEllipsis(mContext.getResources().getString(
                R.string.more_char));
        mPProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * @category 关闭等待框
     */
    protected void dismissProgressDialog() {
        if (progressDialogIsShow)
            mPProgressBar.setVisibility(View.GONE);
        progressDialogIsShow = false;
    }

    /**
     * @category 事件重写
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (progressDialogIsShow) {
                if (null != threadG && threadG.isAlive()) {
                    // 线程中断
                    threadG.interrupt();
                    threadG = null;
                    LogFactory.e("t", "kill Thread");
                }
                dismissProgressDialog();
            } else if (ActivityManager.getLength() > 1) { // 当大于一个页面时首先退出当前页面
                finish();
            }
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }
}
