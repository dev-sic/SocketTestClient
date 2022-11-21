package com.example.socket_test;

import android.app.Application;
import android.content.Context;

import com.example.socket_test.objects.User;
import com.example.socket_test.util.ConnectionThread;

public class TestApplication extends Application {
    private static Context CONTEXT;
    public ConnectionThread connectionThread;

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = this;
    }

    public void executeThread(User user, Context context){
        if(connectionThread == null){
            connectionThread = new ConnectionThread(user, context);
            connectionThread.start();
        }
        connectionThread.user = user;
        connectionThread.mustConnect = true; //연결이 필요한 상태로 변경
    }

    public static TestApplication getInstance(){
        return (TestApplication) CONTEXT;
    }


}
