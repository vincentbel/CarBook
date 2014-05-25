package com.Doric.CarBook;

import android.app.Application;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Sunyao_Will on 2014/5/14.
 */
/*
 *  初始化Application
 */
public class CarBookApplication extends Application {
    private static final String TAG = "JPush";

    @Override
    public void onCreate() {
        Log.d(TAG,"[CarBookApplication] onCreate");
        super.onCreate();

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }
}
