package com.appleby.breakingbad.networkmodel

data class GoogleResult (
    val kind : String,
    val url : Url,
    val queries : Queries,
    val context : Context,
    val searchInformation : SearchInformation,
    val items : List<Items>
)