package com.appleby.pinner.model

import android.content.Context
import io.objectbox.Box
import io.objectbox.BoxStore

object ObjectBox {

    val collectionBox: Box<Collection> by lazy { boxStore.boxFor(Collection::class.java) }
    val pinnedimageBox: Box<PinnedImage> by lazy { boxStore.boxFor(PinnedImage::class.java) }

    lateinit var boxStore: BoxStore
        private set

    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .build()
    }
}