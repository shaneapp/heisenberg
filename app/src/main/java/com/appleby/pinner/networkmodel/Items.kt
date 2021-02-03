package com.appleby.pinner.networkmodel

data class Items (
	val kind : String,
	val title : String,
	val htmlTitle : String,
	val link : String,
	val displayLink : String,
	val snippet : String,
	val htmlSnippet : String,
	val mime : String,
	val fileFormat : String,
	val image : Image
)