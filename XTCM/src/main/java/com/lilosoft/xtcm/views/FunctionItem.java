package com.lilosoft.xtcm.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lilosoft.xtcm.R;

/**
 * @category 自定义Item
 * @author William Liu
 *
 */
public class FunctionItem extends RelativeLayout {

    public ImageView img;
    public TextView text;
    private Context mContext;
    private LayoutInflater mInflater;

    public FunctionItem(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);

    }

    public FunctionItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.FunctionItem);

        int src = typedArray.getResourceId(R.styleable.FunctionItem_src, 0);
        CharSequence text = typedArray.getText(R.styleable.FunctionItem_text);
        typedArray.recycle();

        img.setBackgroundResource(src);
        this.text.setText(text);

    }

    private void init(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);

        mInflater.inflate(R.layout.view_function_item, this);
        img = (ImageView) findViewById(R.id.f_item_img);
        text = (TextView) findViewById(R.id.f_item_text);
    }

}
