package com.appleby.breakingbad.networkmodel

data class Image (
	val contextLink : String,
	val height : Int,
	val width : Int,
	val byteSize : Int,
	val thumbnailLink : String,
	val thumbnailHeight : Int,
	val thumbnailWidth : Int
)