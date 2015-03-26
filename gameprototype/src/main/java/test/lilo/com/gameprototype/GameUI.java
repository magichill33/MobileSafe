package test.lilo.com.gameprototype;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import test.lilo.com.gameprototype.bean.Face;
import test.lilo.com.gameprototype.bean.Man;
import test.lilo.com.gameprototype.bean.MyButton;

/**
 * Created by Administrator on 2015/3/26.
 */
public class GameUI extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder holder;
    private RenderThread renderThread;
    private boolean isRender = true; //控制绘制线程
    /*************************************************************************/
    private Man man;
    private Face face;
    private List<Face> faces;
    private MyButton downButton;

    /*************************************************************************/

    public GameUI(Context context) {
        super(context);
        holder = this.getHolder();
        // The Callback is set with {@link SurfaceHolder#addCallback
        // SurfaceHolder.addCallback} method.
        holder.addCallback(this);
        renderThread = new RenderThread();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        man = new Man(BitmapFactory.decodeResource(getResources(),
                R.drawable.avatar_boy),null);
        faces = new CopyOnWriteArrayList<Face>();

        Point point = new Point(50,getHeight()-80);
        downButton = new MyButton(BitmapFactory.decodeResource(getResources(),
                R.drawable.bottom_default),point,BitmapFactory.decodeResource(getResources(),
                R.drawable.bottom_press));
        downButton.setOnClickListener(new MyButton.OnClickListener() {
            @Override
            public void onClick() {
                man.move(Man.DOWN);
            }
        });
        renderThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRender = false;
    }

    private void drawUI(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        canvas.drawRect(0,0,getWidth(),getHeight(),paint);

        man.draw(canvas);
        drawFace(canvas);
        downButton.draw(canvas);
    }

    private void drawFace(Canvas canvas) {
        for (Face item : faces) {
            item.draw(canvas);
            item.move();

            if (item.getPos().x >= getWidth() || item.getPos().y >= getHeight()) {
                faces.remove(item);// java.util.ConcurrentModificationException
            }
        }
    }

    public void handlerTouch(MotionEvent event) {
        Point touchPos = new Point((int) event.getX(), (int) event.getY());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (downButton.isClick(touchPos)) {
                    // downButton.setClick(true);
                    downButton.click();
                } else {
                    face = man.createFace(BitmapFactory.decodeResource(
                            getResources(), R.drawable.rating_small), touchPos);

                    faces.add(face);
                }

                break;
            case MotionEvent.ACTION_MOVE:
                face = man.createFace(BitmapFactory.decodeResource(getResources(),
                        R.drawable.rating_small), touchPos);

                faces.add(face);
                break;
            case MotionEvent.ACTION_UP:
                downButton.setClick(false);
                break;
        }

    }

    /**
     * drawing thread
     */
    private class RenderThread extends Thread{
        @Override
        public void run() {
            while (isRender){
                // Surface 操作
                // ①/** draw into a surface */
                // public Canvas lockCanvas(Rect dirty)
                // ②在锁定的Canvas进行界面的绘制
                // ③解锁，更新界面
                // public native void unlockCanvas(Canvas canvas);
                // unlock the surface. the screen won't be updated until post()
                // or postAll() is called

                // SurfaceHolder

                // long startTime = System.currentTimeMillis();

                Canvas canvas = null;
                try {
                    canvas = holder.lockCanvas();
                    drawUI(canvas);

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (canvas!=null){
                        holder.unlockCanvasAndPost(canvas);
                    }
                }

                // long endTime = System.currentTimeMillis();

                // 性能评定的参数
                // 每秒界面绘制的次数

                // 1000/(endTime-startTime) FPS : 帧率
                // 如何连贯的界面：FPS>=30
            }
        }
    }
}
