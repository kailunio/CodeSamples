package com.example.NDKDemo;

/**
 * Created with IntelliJ IDEA.
 * User: Real
 * Date: 13-4-9
 * Time: 下午5:24
 * To change this template use File | Settings | File Templates.
 */
public class InvertColor {
    static{
        System.loadLibrary("invertColor");
    }

    public static native void executeByC(int[] img);

    public static void executeByJava(int[] img){
        for(int i=0; i<img.length; i++){
            img[i] = (~ img[i] & 0xffffff00);
        }
    }
}
