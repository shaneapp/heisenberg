package com.appleby.breakingbad.model

import com.appleby.breakingbad.networkmodel.Image
import com.appleby.breakingbad.networkmodel.Items

object DataStore {
    var googleApiUsageCounter = 0
    var imageFilter = "large"
    var lastSearch = arrayListOf<Items>()
//    var collections = arrayListOf<Collection>()
}