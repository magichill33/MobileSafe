package com.ly.kumi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ly.kumi.ConstantValue;
import com.ly.kumi.R;
import com.ly.kumi.util.MediaUtil;

/**
 * Created by magichill33 on 2015/3/14.
 */
public class MySongListAdapter extends BaseAdapter{
    private Context context;

    public MySongListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return MediaUtil.getInstacen().getSongList().size();
    }

    @Override
    public Object getItem(int position) {
        return MediaUtil.getInstacen().getSongList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem, null);
            holder.tx1 = (TextView) convertView.findViewById(R.id.ListItemName);
            holder.tx2 = (TextView) convertView.findViewById(R.id.ListItemContent);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == MediaUtil.CURRENTPOS
                && (MediaUtil.PLAYSTATE == ConstantValue.OPTION_PAUSE || MediaUtil.PLAYSTATE == ConstantValue.OPTION_PLAY)){
            holder.tx1.setTextColor(Color.GREEN);
        }
        holder.tx1.setTag(position);

        holder.tx1.setText((position+1)+"."+MediaUtil.getInstacen().getSongList().get(position).getTitle());
        holder.tx2.setText((MediaUtil.getInstacen().getSongList().get(position)).getArtist());

        return convertView;
    }

    class ViewHolder{
        public TextView tx1;
        public TextView tx2;
    }
}
