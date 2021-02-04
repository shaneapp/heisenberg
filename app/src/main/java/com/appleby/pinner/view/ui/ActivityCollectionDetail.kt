package com.appleby.pinner.view.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.appleby.pinner.CustomDialogs
import com.appleby.pinner.CustomOverlayView
import com.appleby.pinner.DownloadHelper
import com.appleby.pinner.R
import com.appleby.pinner.model.Collection
import com.appleby.pinner.model.ObjectBox
import com.appleby.pinner.model.PinnedImage
import com.appleby.pinner.networkmodel.Items
import com.appleby.pinner.view.adapter.ImageResultsListAdapter
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.activity_collection_detail.*

class ActivityCollectionDetail : AppCompatActivity() {

    private lateinit var viewer: StfalconImageViewer<Items>

    private var overlayView: CustomOverlayView? = null

    private var parentCollection: Collection? = null

    private val imagesInCollectionAdapter =
        ImageResultsListAdapter(this,
            itemClick = { index, target ->

                overlayView = CustomOverlayView(this).apply {
                    update(parentCollection, parentCollection?.pinnedimages!!.get(index))
                }

                StfalconImageViewer.Builder<PinnedImage>(
                    this,
                    parentCollection?.pinnedimages
                ) { imageView, pinnedImage ->
                    Glide.with(this@ActivityCollectionDetail).load(pinnedImage.imageUrl)
                        .into(imageView)
                }
                    .withOverlayView(overlayView)
                    .withStartPosition(index)
                    .allowZooming(true)
                    .withTransitionFrom(target)
                    .withImageChangeListener {
                        overlayView?.update(
                            parentCollection,
                            parentCollection?.pinnedimages!!.get(index)
                        )
                    }
                    .show()

            }, itemLongPress = { index: Int, target: ImageView ->

                CustomDialogs.showDeleteDialog(this) {
                    val imageToDelete = parentCollection?.pinnedimages!!.get(index)
                    parentCollection?.pinnedimages!!.remove(imageToDelete)
                    ObjectBox.pinnedimageBox.remove(imageToDelete)
                    loadCollectionDetails(parentCollection)
                }

                true

            })

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

        collection?.let { loadedCollection ->
            tvCollectionTitle.text = loadedCollection.name?.toUpperCase()

            ivAddImages.setOnClickListener {
                startActivity(ActivityImageSearch.prepareIntent(this, collection.id))
            }

            ivShareCollection.setOnClickListener {
                var sharedString = ""
                loadedCollection.pinnedimages.forEach {
                    sharedString += it.imageUrl + System.lineSeparator()
                }

                val shareLink = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "Image Link")
                    putExtra(Intent.EXTRA_TEXT, sharedString)
                }
                startActivity(Intent.createChooser(shareLink, "Share Link"))
            }

            ivDownloadAll.setOnClickListener {
                for (image in loadedCollection.pinnedimages) {
                    DownloadHelper.downloadPinnedImage(this, image, loadedCollection.name!!)
                }
            }

            imagesInCollectionAdapter.updateData(loadedCollection.pinnedimages)
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