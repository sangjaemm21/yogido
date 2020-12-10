package com.example.food;

import android.app.Application;

public class MyApplication extends Application {

    private String hostAddr;

    @Override
    public void onCreate() {
        //전역 변수 초기화
        hostAddr = "http://017e5f728649.ngrok.io";
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    public String getState(){
        return hostAddr;
    }

}
