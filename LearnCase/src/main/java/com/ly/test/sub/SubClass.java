package com.ly.test.sub;

import com.ly.test.parent.PClass;

/**
 * Created by magichill33 on 2015/3/30.
 */
public class SubClass extends PClass{
    public int i = 2;
    public SubClass() {
        super();
    }

    @Override
    public void m() {
        System.out.println("B");
    }

    public void echo2()
    {
        System.out.print(i);
    }
}
