package com.appleby.pinner

import android.content.Context
import android.os.Environment
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.appleby.pinner.model.PinnedImage
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader

object DownloadHelper {

    fun downloadPinnedImage(context: Context, pinnedImage: PinnedImage, subdirectoryName: String = "", showToasts: Boolean = false) {
        var downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.path
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z')

        if (subdirectoryName.isNotEmpty()) {
            downloadDir += "/$subdirectoryName"
        }

        val filename = (1..20)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")

        val ext = MimeTypeMap.getSingleton().getExtensionFromMimeType(pinnedImage.imageMimeType)

        PRDownloader
            .download(pinnedImage.imageUrl, downloadDir, "${filename}.${ext}")
            .build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    if (showToasts) {
                        Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onError(error: Error?) {
                    if (showToasts) {
                        Toast.makeText(context, "Error downloading", Toast.LENGTH_SHORT).show()
                    }
                }

            })
    }

}