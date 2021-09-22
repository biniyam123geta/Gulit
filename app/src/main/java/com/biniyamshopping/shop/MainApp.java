package com.biniyamshopping.shop;

import android.app.Application;
import android.content.Context;

public class MainApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.oAattach(base,"am"));
    }
}
