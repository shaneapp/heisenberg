package com.appleby.breakingbad.view.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.appleby.breakingbad.R
import com.appleby.breakingbad.model.DataStore
import com.appleby.breakingbad.networkmodel.Items
import com.appleby.breakingbad.view.adapter.ImageResultsListAdapter
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.activity_collection_detail.*
import kotlinx.android.synthetic.main.activity_image_search.*

class ActivityCollectionDetail : AppCompatActivity() {

    private lateinit var viewer: StfalconImageViewer<Items>

    private val imagesInCollectionAdapter =
        ImageResultsListAdapter(this) { index, target ->
//            startActivity(ActivityCharacterDetail.prepareIntent(this, it))
            StfalconImageViewer.Builder<Items>(this, DataStore.lastSearch) { imageView, item ->
                Glide.with(this@ActivityCollectionDetail).load(item.link).into(imageView)
            }
                .withStartPosition(index)
                .allowZooming(true)
                .withTransitionFrom(target)
                .show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_detail)

        val staggeredLayout = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredLayout.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        rvCollectionImages.layoutManager = staggeredLayout
        rvCollectionImages.adapter = imagesInCollectionAdapter

        ivAddImages.setOnClickListener {
            startActivity(ActivityImageSearch.prepareIntent(this, 0))
        }
    }

    override fun onResume() {
        super.onResume()

        //val selectedIndex = intent.getIntExtra(EXTRA_SELECTED_IMAGE_INDEX, 0)
//        imagesInCollectionAdapter.updateData(filteredCharacters)
    }

    companion object {
        private const val EXTRA_SELECTED_IMAGE_INDEX = "EXTRA_IMAGE_INDEX"

        fun prepareIntent(context: Context, index: Int) : Intent {
            val intent = Intent(context, ActivityCollectionDetail::class.java)
            intent.putExtra(EXTRA_SELECTED_IMAGE_INDEX, index)
            return intent
        }
    }

}