package com.ly.drawbitmap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

    private ImageView iv;
    private Bitmap alertBitmap;
    private Canvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.pre);
        //创建一个空白的原图的拷贝
        alertBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),
                bitmap.getConfig());
        final Canvas canvas = new Canvas(alertBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(bitmap,new Matrix(),paint);
        iv.setImageBitmap(alertBitmap);
        iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        System.out.println("action down");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        System.out.println("设置("+x+","+y+")透明颜色");
                        for (int i=-4;i<5;i++){
                            for (int j=-4;j<5;j++){
                                try{
                                    alertBitmap.setPixel(x+i,y+j,Color.TRANSPARENT);
                                }catch (Exception e){

                                }

                            }
                        }
                        iv.setImageBitmap(alertBitmap);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });
    }

}
