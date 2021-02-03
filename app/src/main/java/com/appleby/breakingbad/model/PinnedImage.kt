package com.appleby.breakingbad.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class PinnedImage(
    @Id var id: Long = 0,
    var imageUrl: String? = null,
    var imageMimeType: String? = null,
    var thumbUrl: String? = null
) {
    lateinit var collection: ToOne<Collection>
}