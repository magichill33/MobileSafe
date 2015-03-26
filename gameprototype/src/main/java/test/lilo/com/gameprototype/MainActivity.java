package test.lilo.com.gameprototype;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;


public class MainActivity extends Activity {
    private GameUI gameUI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameUI = new GameUI(this);
        setContentView(gameUI);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gameUI.handlerTouch(event);
        return super.onTouchEvent(event);
    }
}
