package com.ly.test;

import android.content.SyncStatusObserver;

import com.ly.test.parent.PClass;
import com.ly.test.sub.SubClass;

/**
 * Created by magichill33 on 2015/3/30.
 */
public class TestClass {
    // public static final void m4();
    //public abstract synchronized  void m3();
    //public abstract native void m2();
    //public abstract static void m1();
    static int i;
    public static void main(String[] args){
       // System.out.println(new TestClass().tryInfo());
       /* PClass pClass1 = new PClass();
        pClass1.m();
        PClass pClass2 = new SubClass();
        pClass2.m();
        SubClass subClass1 = (SubClass) pClass2;
        subClass1.m();
        SubClass subClass2 = new SubClass();
        subClass2.m();*/
      /*  for (int j=0;j<5;j++){
            System.out.print(i);
            i++;
        }*/
        /*String s = "abcdefghij";
        System.out.println(s.lastIndexOf("ef"));*/
        /*PClass child = new SubClass();
        child.echo();
        child.echo2();
        System.out.println(child.i);

        SubClass child2 = new SubClass();
        child2.echo();
        child2.echo2();
        System.out.println(child2.i);*/

       /* int i = 1;
        int j = i++;
        if ((i>j)&&(i++==j)){
            i+=j;
        }
        System.out.println(i);*/
        int i= 0;
        for (foo('A');foo('B')&&(i<2);foo('C'))
        {
            i++;
            foo('D');
        }
        //ABDCBDCB
    }

    static boolean foo(char c){
        System.out.print(c);
        return true;
    }


    static protected int method1(int a,int b)
    {
        return 1;
    }

    public short method2(int a,int b)
    {
        return 0;
    }

    private int mehtod3(int a,int b)
    {
        return 0;
    }



    public String tryInfo()
    {
        try{
            return "hello";
        }finally {
            System.out.println("world");
        }
    }
}
