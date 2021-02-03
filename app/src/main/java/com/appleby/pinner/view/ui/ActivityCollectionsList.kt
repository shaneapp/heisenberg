package com.appleby.pinner.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.appleby.pinner.CustomDialogs
import com.appleby.pinner.R
import com.appleby.pinner.model.Collection
import com.appleby.pinner.model.ObjectBox
import com.appleby.pinner.view.adapter.CollectionsListAdapter
import kotlinx.android.synthetic.main.activity_collections.*

class ActivityCollectionsList : AppCompatActivity() {

    private val collectionListAdapter = CollectionsListAdapter(this) {
        startActivity(ActivityCollectionDetail.prepareIntent(this, it.id))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collections)

        ivAddCollection.setOnClickListener {
            CustomDialogs.addFolder(this) {

                val newCollection = Collection(name = it)
                ObjectBox.collectionBox.put(newCollection)
                refreshCollectionList()

            }
        }

        rvCollections.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvCollections.adapter = collectionListAdapter
    }

    override fun onResume() {
        super.onResume()

        refreshCollectionList()
    }

    fun refreshCollectionList() {
        collectionListAdapter.updateData(ObjectBox.collectionBox.all)
    }

}