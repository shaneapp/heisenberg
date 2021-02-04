package com.appleby.pinner

import android.app.Application
import com.appleby.pinner.model.ObjectBox
import com.appleby.pinner.model.Settings
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import java.util.*


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

        ObjectBox.settingsBox.put(Settings(apiRequestCount = 100, apiRequestsLastUpdated = Date()))
    }

}