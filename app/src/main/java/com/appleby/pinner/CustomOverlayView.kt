package com.appleby.pinner

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Environment
import android.util.AttributeSet
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.appleby.pinner.model.Collection
import com.appleby.pinner.model.ObjectBox
import com.appleby.pinner.model.PinnedImage
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import kotlinx.android.synthetic.main.view_overlay_gallery.view.*


class CustomOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

//    var onShareClick: (Items) -> Unit = {}

    init {
        View.inflate(context, R.layout.view_overlay_gallery, this)
        setBackgroundColor(Color.TRANSPARENT)
    }

    fun update(collection: Collection?, pinnedImage: PinnedImage) {
        ivAdd.setOnClickListener {
            collection?.pinnedimages?.add(PinnedImage(imageUrl = pinnedImage.imageUrl, imageMimeType = pinnedImage.imageMimeType, thumbUrl = pinnedImage.thumbUrl))
            ObjectBox.collectionBox.put(collection)
            Toast.makeText(context, "Added to ${collection?.name}", Toast.LENGTH_SHORT).show()
        }

        ivShare.setOnClickListener {
            val shareLink = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Image Link")
                putExtra(Intent.EXTRA_TEXT, pinnedImage.imageUrl)
            }
            context.startActivity(Intent.createChooser(shareLink, "Share Link"))
        }

        ivDownload.setOnClickListener {
            DownloadHelper.downloadPinnedImage(context, pinnedImage, "", true)
        }
    }
}