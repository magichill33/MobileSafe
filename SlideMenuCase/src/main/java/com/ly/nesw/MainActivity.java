package com.ly.nesw;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.ly.nesw.fragment.Fragment1;
import com.ly.nesw.fragment.MenuFragment;
import com.ly.nesw.fragment.RightMenuFragment;


public class MainActivity extends SlidingFragmentActivity {
    private SlidingMenu slidingMenu;
    /**
     * 1 得到滑动菜单
     * 2 设置滑动菜单是在左边出来还是右边出来
     * 3 设置滑动菜单出来之后，内容页，显示的剩余宽度
     * 4 设置滑动菜单的阴影 设置阴影，阴影需要在开始的时候，特别暗，慢慢的变淡
     * 5 设置阴影的宽度
     * 6 设置滑动菜单的范围
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //去掉界面标题
        setContentView(R.layout.content);
        setBehindContentView(R.layout.menu); //设置侧滑菜单的布局

        Fragment fragment1 = new Fragment1();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
       /* getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                fragment1).commit();*/
        transaction.replace(R.id.content_frame,fragment1);
        transaction.addToBackStack(null);
        transaction.commit();

        slidingMenu = getSlidingMenu();
        //2 设置滑动菜单是在左边出来还是右边出来
        //参数可以设置左边LEFT，也可以设置右边RIGHT ，还能设置左右LEFT_RIGHT
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        //3 设置滑动菜单出来之后，内容页，显示的剩余宽度
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        //4 设置滑动菜单的阴影 设置阴影，阴影需要在开始的时候，特别暗，慢慢的变淡
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        //5 设置阴影的宽度
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        //6 设置滑动菜单的范围
        // 第一个参数 SlidingMenu.TOUCHMODE_FULLSCREEN 可以全屏滑动
        // 第二个参数 SlidingMenu.TOUCHMODE_MARGIN 只能在边沿滑动
        // 第三个参数 SlidingMenu.TOUCHMODE_NONE 不能滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //创建fragment
        MenuFragment menuFragment = new MenuFragment();
        //获取fragment的管理者
        getSupportFragmentManager()
                //开启事物
                .beginTransaction()
                        //替换
                .replace(R.id.menu_frame, menuFragment, "Menu")
                        //提交
                .commit();

        slidingMenu.setSecondaryMenu(R.layout.right_menu);
        slidingMenu.setSecondaryShadowDrawable(R.drawable.shadowright);
        RightMenuFragment rightMenuFragment = new RightMenuFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.right_menu_frame, rightMenuFragment).commit();
    }

    /**
     * 回调方法
     * @param f
     */
    public void switchFragment(Fragment f) {
        //进行fragment的切换
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, f).commit();
        //自动切换
        slidingMenu.toggle();
    }
}
