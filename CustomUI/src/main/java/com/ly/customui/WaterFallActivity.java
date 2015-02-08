package com.ly.customui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;


public class WaterFallActivity extends Activity {

    private ListView lv1;
    private ListView lv2;
    private ListView lv3;

    private int ids[] = {
      R.drawable.default1,
            R.drawable.girl1,
            R.drawable.girl2,
            R.drawable.girl3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_fall);

        lv1 = (ListView) findViewById(R.id.lv1);
        lv2 = (ListView) findViewById(R.id.lv2);
        lv3 = (ListView) findViewById(R.id.lv3);

        lv1.setAdapter(new MyAdapter());
        lv2.setAdapter(new MyAdapter());
        lv3.setAdapter(new MyAdapter());
    }

    class MyAdapter  extends BaseAdapter{

        @Override
        public int getCount() {
            return 3000;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView iv = null;
            if (convertView ==null){
                iv = (ImageView) View.inflate(getApplicationContext(),R.layout.lv_item,null);
            }else {
                iv = (ImageView) convertView;
            }

            int resId = (int) (Math.random()*4);
            iv.setImageResource(ids[resId]);
            return iv;
        }
    }
}
