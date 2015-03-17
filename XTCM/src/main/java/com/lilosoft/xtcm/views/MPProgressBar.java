package com.lilosoft.xtcm.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lilosoft.xtcm.R;

/**
 * @category 自定义等待框
 * @author William Liu
 *
 */
public class MPProgressBar extends RelativeLayout {

    private ProgressBar progressBar;
    private TextView tvTipMsg;
    private TextView tvEllipsis;
    private Context mContext;
    private LayoutInflater mInflater;

    public MPProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MPProgressBar(Context context) {
        super(context);
        init(context);
    }
    public MPProgressBar(Context context, int mStyle) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mInflater.inflate(R.layout.view_mp_progressbar, this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tvTipMsg = (TextView) findViewById(R.id.tvTipMsg);
        tvEllipsis = (TextView) findViewById(R.id.tvEllipsis);

        tvTipMsg.setTextColor(Color.WHITE);
        tvEllipsis.setTextColor(Color.WHITE);
    }

    /**
     * 设置Style
     * @param style
     */
    public void setProgressBarStyle(int style) {
        progressBar.setScrollBarStyle(style);
    }

    /**
     * 设置提示信息
     * @param tipMsg
     */
    public void setTextTipMsg (CharSequence tipMsg) {
        tvTipMsg.setText(tipMsg);
    }

    /**
     * 设置文字后面的省略号，如：通信中…
     * @param ellipsis
     */
    public void setTextEllipsis (CharSequence ellipsis) {
        tvEllipsis.setText(ellipsis);
    }

    /**
     * 设置字体颜色
     * @param textColor
     */
    public void setTextColorTipMsg (int textColor) {
        tvTipMsg.setTextColor(textColor);
        tvEllipsis.setTextColor(textColor);
    }

    /**
     * 设置字体大小
     * @param textSize
     */
    public void setTextSizeTipMsg (float  textSize) {
        tvTipMsg.setTextSize(textSize);
        tvEllipsis.setTextSize(textSize);
    }

}
