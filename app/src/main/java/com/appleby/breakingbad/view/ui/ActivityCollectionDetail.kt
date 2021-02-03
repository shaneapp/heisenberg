package com.appleby.breakingbad.view.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.appleby.breakingbad.CustomOverlayView
import com.appleby.breakingbad.R
import com.appleby.breakingbad.model.Collection
import com.appleby.breakingbad.model.DataStore
import com.appleby.breakingbad.model.ObjectBox
import com.appleby.breakingbad.model.PinnedImage
import com.appleby.breakingbad.networkmodel.Items
import com.appleby.breakingbad.view.adapter.ImageResultsListAdapter
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.activity_collection_detail.*

class ActivityCollectionDetail : AppCompatActivity() {

    private lateinit var viewer: StfalconImageViewer<Items>

    private var overlayView: CustomOverlayView? = null

    private var parentCollection: Collection? = null

    private val imagesInCollectionAdapter =
        ImageResultsListAdapter(this) { index, target ->

            overlayView = CustomOverlayView(this).apply {
                update(parentCollection, parentCollection?.pinnedimages!!.get(index))
            }

            StfalconImageViewer.Builder<PinnedImage>(this, parentCollection?.pinnedimages) { imageView, pinnedImage ->
                Glide.with(this@ActivityCollectionDetail).load(pinnedImage.imageUrl).into(imageView)
            }
                .withOverlayView(overlayView)
                .withStartPosition(index)
                .allowZooming(true)
                .withTransitionFrom(target)
                .withImageChangeListener {
                    overlayView?.update(parentCollection, parentCollection?.pinnedimages!!.get(index))
                }
                .show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_detail)

        val staggeredLayout = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredLayout.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        rvCollectionImages.layoutManager = staggeredLayout
        rvCollectionImages.adapter = imagesInCollectionAdapter
    }

    override fun onResume() {
        super.onResume()

        val selectedCollectionId = intent.getLongExtra(EXTRA_SELECTED_COLLECTION_ID, -1)
        if (selectedCollectionId != -1L) {
            parentCollection = ObjectBox.collectionBox[selectedCollectionId]
            loadCollectionDetails(parentCollection)
        }
    }

    fun loadCollectionDetails(collection: Collection?) {

        collection?.let {
            tvCollectionTitle.text = it.name?.toUpperCase()

            ivAddImages.setOnClickListener {
                startActivity(ActivityImageSearch.prepareIntent(this, collection.id))
            }

            imagesInCollectionAdapter.updateData(it.pinnedimages)
        }

    }

    companion object {
        private const val EXTRA_SELECTED_COLLECTION_ID = "EXTRA_COLLECTION_ID"

        fun prepareIntent(context: Context, collectionId: Long) : Intent {
            val intent = Intent(context, ActivityCollectionDetail::class.java)
            intent.putExtra(EXTRA_SELECTED_COLLECTION_ID, collectionId)
            return intent
        }
    }

}