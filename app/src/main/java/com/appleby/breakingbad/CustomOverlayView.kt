package com.appleby.breakingbad

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.appleby.breakingbad.model.Collection
import com.appleby.breakingbad.model.ObjectBox
import com.appleby.breakingbad.model.PinnedImage
import com.appleby.breakingbad.networkmodel.Items
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

    fun update(collection: Collection?, items: Items) {
        ivShare.setOnClickListener {
            collection?.pinnedimages?.add(PinnedImage(imageUrl = items.link, thumbUrl = items.image.thumbnailLink))
            ObjectBox.collectionBox.put(collection)
            Toast.makeText(context, "Added to ${collection?.name}", Toast.LENGTH_SHORT).show()
        }
    }
}