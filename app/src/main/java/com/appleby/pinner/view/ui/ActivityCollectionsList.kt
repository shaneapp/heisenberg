package com.appleby.pinner.view.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.appleby.pinner.CustomDialogs
import com.appleby.pinner.R
import com.appleby.pinner.model.Collection
import com.appleby.pinner.model.ObjectBox
import com.appleby.pinner.view.adapter.CollectionsListAdapter
import kotlinx.android.synthetic.main.activity_collections.*
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.*


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

        ivDownloadFolder.setOnClickListener {
            val downloadDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.path

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(downloadDir), "resource/folder")

            if (intent.resolveActivityInfo(packageManager, 0) != null) {
                startActivity(intent)
            } else {
                // if you reach this place, it means there is no any file
                // explorer app installed on your device
            }
        }

        rvCollections.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvCollections.adapter = collectionListAdapter
    }

    override fun onResume() {
        super.onResume()

        refreshCollectionList()
        resetApiCounter()
    }

    fun resetApiCounter() {
        val timeZone = DateTimeZone.forID("America/Los_Angeles")
        val midnightInPT = DateTime(timeZone).plusDays(1).withTimeAtStartOfDay()
        val lastUpdatedTimeUTC = DateTime(ObjectBox.settingsBox[1].apiRequestsLastUpdated)
        if (lastUpdatedTimeUTC.isBefore(midnightInPT) && DateTime.now().isAfter(midnightInPT)) {
            val settings = ObjectBox.settingsBox[0]
            settings.apiRequestCount = 100
            settings.apiRequestsLastUpdated = Date()
            ObjectBox.settingsBox.put(settings)
        }
    }

    fun refreshCollectionList() {
        collectionListAdapter.updateData(ObjectBox.collectionBox.all)
    }

}