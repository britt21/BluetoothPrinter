package com.example.printtooth;

import android.app.Application;
import android.widget.Button;

import com.mazenrashed.printooth.Printooth;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Printooth.INSTANCE.init(this);
    }
}