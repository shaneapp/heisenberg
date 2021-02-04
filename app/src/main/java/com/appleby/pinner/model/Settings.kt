package com.appleby.pinner.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.*

@Entity
data class Settings(
    @Id var id: Long = 0,
    var apiRequestCount : Int = 0,
    var apiRequestsLastUpdated : Date? = null
)