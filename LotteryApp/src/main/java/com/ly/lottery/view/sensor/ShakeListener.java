package com.ly.lottery.view.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.os.Vibrator;

/**
 * 处理传感器监听
 * Created by Administrator on 2015/2/12.
 */
public abstract class ShakeListener implements SensorEventListener{
    private Context context;
    private Vibrator vibrator;
    private float lastX;
    private float lastY;
    private float lastZ;
    private long lasttime;
    private long duration = 100;
    private float total; //累加
    private float switchValue = 200; //判断手机摇晃的阈值
    protected ShakeListener(Context context) {
        this.context = context;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    // ①记录第一个数据：三个轴的加速度，为了屏蔽掉不同手机采样的时间间隔，记录第一个点的时间
    // ②当有新的传感器数据传递后，判断时间间隔（用当前时间与第一个采样时间进行比对，如果满足了时间间隔要求，认为是合格的第二个点，否则舍弃该数据包）
    // 进行增量的计算：获取到新的加速度值与第一个点上存储的进行差值运算，获取到一点和二点之间的增量
    // ③以此类推，获取到相邻两个点的增量，一次汇总
    // ④通过汇总值与设定好的阈值比对，如果大于等于，用户摇晃手机，否则继续记录当前的数据（加速度值和时间）

    @Override
    public void onSensorChanged(SensorEvent event) {
        //判断：是否是第一个点
        if (lasttime==0){
            lastX = event.values[SensorManager.DATA_X];
            lastY = event.values[SensorManager.DATA_Y];
            lastZ = event.values[SensorManager.DATA_Z];

            lasttime = System.currentTimeMillis();

        }else {
            long currenttime = System.currentTimeMillis();
            //尽可能屏蔽掉不同手机差异
            if ((currenttime - lasttime) >=duration){
                //第二点及以后
                float x = event.values[SensorManager.DATA_X];
                float y = event.values[SensorManager.DATA_Y];
                float z = event.values[SensorManager.DATA_Z];

                float dx = Math.abs(x-lastX);
                float dy = Math.abs(y-lastY);
                float dz = Math.abs(z-lastZ);

                if (dx < 1) {
                    dx = 0;
                }
                if (dy < 1) {
                    dy = 0;
                }
                if (dz < 1) {
                    dz = 0;
                }
                // 极个别的手机，静止某个轴的增量大于1,10以上100以上
                if(dx==0||dy==0||dz==0)
                {
                    init();
                }

                //一点和二点总增量
                float shake = dx + dy + dz;
                if (shake == 0)
                {
                    init();
                }

                total += shake;
                if (total>=switchValue){
                    //摇晃手机处理
                    randomCure();
                    //提示用户
                    vibrator.vibrate(100);
                    //所有的数据都要初始化
                    init();
                }else {
                    lastX = event.values[SensorManager.DATA_X];
                    lastY = event.values[SensorManager.DATA_Y];
                    lastZ = event.values[SensorManager.DATA_Z];

                    lasttime = System.currentTimeMillis();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void init(){
        lastX = 0;
        lastY = 0;
        lastZ = 0;
        lasttime = 0;
        total = 0;
    }

    //机选一注彩票
    public abstract void randomCure();
}
