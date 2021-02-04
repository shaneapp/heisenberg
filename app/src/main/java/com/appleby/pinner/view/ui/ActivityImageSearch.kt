package com.appleby.pinner.view.ui

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.appleby.pinner.CustomOverlayView
import com.appleby.pinner.R
import com.appleby.pinner.model.Collection
import com.appleby.pinner.model.DataStore
import com.appleby.pinner.model.ObjectBox
import com.appleby.pinner.model.PinnedImage
import com.appleby.pinner.networkmodel.Items
import com.appleby.pinner.view.adapter.ImageResultsListAdapter
import com.appleby.pinner.viewmodel.ImageSearchViewModel
import com.appleby.pinner.viewmodel.ImageSearchState
import com.bumptech.glide.Glide
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.activity_image_search.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Period

class ActivityImageSearch : AppCompatActivity() {

    private val viewModel: ImageSearchViewModel by viewModels()

    private lateinit var viewer: StfalconImageViewer<Items>

    private var overlayView: CustomOverlayView? = null

    private var parentCollection: Collection? = null

    private val characterListAdapter =
        ImageResultsListAdapter(this, { index, target ->

            val networkImage = DataStore.lastSearch[index]
            val clickedPinnedImage = PinnedImage(imageUrl = networkImage.link, imageMimeType = networkImage.mime, thumbUrl = networkImage.image.thumbnailLink)

            overlayView = CustomOverlayView(this).apply {
                update(parentCollection, clickedPinnedImage)
            }

            StfalconImageViewer.Builder<Items>(this, DataStore.lastSearch) { imageView, item ->
                Glide.with(this@ActivityImageSearch).load(item.link).into(imageView)
            }
                .withOverlayView(overlayView)
                .withStartPosition(index)
                .allowZooming(true)
                .withTransitionFrom(target)
                .withImageChangeListener {
                    val changedToNetworkImage = DataStore.lastSearch[it]
                    val changedToClickedPinnedImage = PinnedImage(imageUrl = changedToNetworkImage.link, imageMimeType = changedToNetworkImage.mime, thumbUrl = changedToNetworkImage.image.thumbnailLink)
                    overlayView?.update(parentCollection, changedToClickedPinnedImage)
                }
                .show()
        }, { _, _ ->
            true
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_search)

        viewModel.result.observe(this, Observer {
            when(it) {
                is ImageSearchState.NetworkSuccess -> updateRecyclerView()
                is ImageSearchState.NetworkFailure -> {
                    Toast.makeText(this, "Request failed", Toast.LENGTH_SHORT).show()
                }
            }
        })


        val staggeredLayout = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredLayout.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        rvCharacterList.layoutManager = staggeredLayout
        rvCharacterList.adapter = characterListAdapter
//        rvCharacterList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    viewModel.performImageSearch(etSearch.text.toString(), characterListAdapter.itemCount)
//                }
//            }
//        })

        etSearch.doOnTextChanged { _, _, _, _ ->
            //viewModel.requestFilterRefresh()
        }

        buttonLoadMore.setOnClickListener {
            viewModel.performImageSearch(etSearch.text.toString(), characterListAdapter.itemCount)
        }

        etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.clearPreviousSearchCache()
                    viewModel.performImageSearch(etSearch.text.toString(), characterListAdapter.itemCount)
                    return true
                }
                return false
            }
        })

        ivCross.setOnClickListener { etSearch.text.clear() }

        ivFilter.setOnClickListener {
            showSeasonFilter()
        }

        etSearch.requestFocus()
    }

    override fun onResume() {
        super.onResume()

        val parentCollectionId = intent.getLongExtra(ActivityImageSearch.EXTRA_PARENT_COLLECTION_ID, -1)
        if (parentCollectionId != -1L) {
            parentCollection = ObjectBox.collectionBox[parentCollectionId]
        }

        refreshUsageInfo()
    }

    private fun refreshUsageInfo() {
        val timeZone = DateTimeZone.forID("America/Los_Angeles")
        val midnightInPT = DateTime(timeZone).plusDays(1).withTimeAtStartOfDay()
        val timeBetweenNowAndMidnightPT = Period(DateTime.now(), midnightInPT)
        val remainingRequests = ObjectBox.settingsBox[1].apiRequestCount
        if (remainingRequests < 1) {
            tvApiUsage.text =
                "Usage resets in ${timeBetweenNowAndMidnightPT.hours}h ${timeBetweenNowAndMidnightPT.minutes}m"
        } else {
            tvApiUsage.text = remainingRequests.toString()
        }
    }

    private fun updateRecyclerView() {
        var filteredCharacters = DataStore.lastSearch.map { PinnedImage(imageUrl = it.link, thumbUrl = it.image.thumbnailLink) }

        refreshUsageInfo()

//        val nameFilter = etSearch.text
//        if (nameFilter.isNotEmpty()) {
//            filteredCharacters = filteredCharacters.filter { it.name.toLowerCase().contains(nameFilter) }
//        }
//
//        if (season > 0) {
//            filteredCharacters = filteredCharacters.filter { !it.appearance.isNullOrEmpty() }
//                .filter { it.appearance.contains(season) }
//        }

        characterListAdapter.updateData(filteredCharacters)
    }

    private fun showSeasonFilter() {
        val imageSizeCode = arrayOf("icon", "small", "medium", "large", "xlarge", "xxlarge", "huge")
        val imageSizeDisplay = arrayOf("Icon", "Small", "Medium", "Large", "Extra Large", "Super Large", "Huge")
        val seasonDialog = AlertDialog.Builder(this)
            .setTitle("Image Size")
            .setItems(imageSizeDisplay, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                    viewModel.clearPreviousSearchCache()
                    viewModel.updateImageFilter(imageSizeCode[which])
                    viewModel.performImageSearch(etSearch.text.toString(), characterListAdapter.itemCount)
                }
            })

        seasonDialog.show()
    }

    companion object {
        private const val EXTRA_PARENT_COLLECTION_ID = "EXTRA_PARENT_COLLECTION_ID"

        fun prepareIntent(context: Context, collectionId: Long) : Intent {
            val intent = Intent(context, ActivityImageSearch::class.java)
            intent.putExtra(EXTRA_PARENT_COLLECTION_ID, collectionId)
            return intent
        }
    }

}