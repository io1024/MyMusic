// IMusicAidlInterface.aidl
package com.zero.music.service;

// Declare any non-default types here with import statements
// 自定义接口,封装中间帮助类对象要实现的方法
interface IMusicAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

     void callPlayer();
     void callRePlayer();
     void callPause();
}
