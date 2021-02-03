package com.appleby.pinner.model

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
data class Collection(
    @Id var id: Long = 0,
    var name : String? = null
) {
    @Backlink(to = "collection")
    lateinit var pinnedimages: ToMany<PinnedImage>
}