package com.appleby.breakingbad.viewmodel

sealed class ImageSearchState {
    object NetworkSuccess : ImageSearchState()
    object NetworkFailure : ImageSearchState()
}