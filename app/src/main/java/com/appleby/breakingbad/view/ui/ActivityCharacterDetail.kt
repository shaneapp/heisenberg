package com.appleby.breakingbad.view.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appleby.breakingbad.R
import com.appleby.breakingbad.model.DataStore
import com.appleby.breakingbad.networkmodel.Items
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.activity_detail.*

class ActivityCharacterDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

    }

    override fun onResume() {
        super.onResume()

        val selectedIndex = intent.getIntExtra(EXTRA_SELECTED_IMAGE_INDEX, 0)

        val imageViewer = StfalconImageViewer.Builder<Items>(this, DataStore.lastSearch) { view, image ->
            Glide.with(this@ActivityCharacterDetail).load(image.link).into(ivPreview)
        }
            .withStartPosition(selectedIndex)
            .allowSwipeToDismiss(true)
            .allowZooming(true)
            .show(true)
    }

    companion object {
        private const val EXTRA_SELECTED_IMAGE_INDEX = "EXTRA_IMAGE_INDEX"

        fun prepareIntent(context: Context, index: Int) : Intent {
            val intent = Intent(context, ActivityCharacterDetail::class.java)
            intent.putExtra(EXTRA_SELECTED_IMAGE_INDEX, index)
            return intent
        }
    }

}