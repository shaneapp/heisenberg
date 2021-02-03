package com.appleby.pinner

import android.app.Application
import com.appleby.pinner.model.ObjectBox
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig




class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ObjectBox.init(this)

        val config = PRDownloaderConfig.newBuilder()
            .setDatabaseEnabled(true)
            .setReadTimeout(30000)
            .setConnectTimeout(30000)
            .build()
        PRDownloader.initialize(this, config)
    }

}