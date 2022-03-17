package com.gulitshopping.shop.app;

import android.app.Application;

import com.gulitshopping.shop.offline.ObjectBox;

public class Gulit extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ObjectBox.init(this);


    }

}
