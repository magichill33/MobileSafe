package com.ly.ndk.module;

/**
 * Created by Administrator on 2015/3/20.
 */
public class SayHello {
    public native String getHelloFromC();
    static{
        System.loadLibrary("JniTest");
    }
}
