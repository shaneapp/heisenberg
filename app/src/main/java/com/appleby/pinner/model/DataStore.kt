package com.appleby.pinner.model

import com.appleby.pinner.networkmodel.Items

object DataStore {
    var googleApiUsageCounter = 0
    var imageFilter = "large"
    var lastSearch = arrayListOf<Items>()
//    var collections = arrayListOf<Collection>()
}