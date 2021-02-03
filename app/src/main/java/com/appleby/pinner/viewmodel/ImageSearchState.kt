package com.appleby.pinner.viewmodel

sealed class ImageSearchState {
    object NetworkSuccess : ImageSearchState()
    object NetworkFailure : ImageSearchState()
}