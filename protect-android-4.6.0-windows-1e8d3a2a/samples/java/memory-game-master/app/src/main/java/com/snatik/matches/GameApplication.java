package com.snatik.matches;

import android.app.Application;
import android.content.Context;

import com.snatik.matches.utils.FontLoader;

public class GameApplication extends Application {
    public static Application sAppInstance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sAppInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FontLoader.loadFonts(this);
    }
}
