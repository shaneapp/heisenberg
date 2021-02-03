package com.appleby.breakingbad

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
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

    fun update(items: Items) {
        ivShare.setOnClickListener { Toast.makeText(context, "Yolo", Toast.LENGTH_SHORT).show() }
    }
}