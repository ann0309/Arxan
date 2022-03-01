package com.arxan.simplesimon

import android.app.Application
import android.content.Context

class SimpleSimonApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        sAppInstance = this
    }

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        lateinit var sAppInstance: SimpleSimonApplication
    }
}