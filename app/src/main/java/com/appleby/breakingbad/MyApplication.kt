package com.appleby.breakingbad

import android.app.Application
import com.appleby.breakingbad.model.ObjectBox

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ObjectBox.init(this)
    }

}