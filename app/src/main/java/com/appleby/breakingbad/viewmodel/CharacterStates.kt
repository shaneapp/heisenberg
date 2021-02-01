package com.appleby.breakingbad.viewmodel

import com.appleby.breakingbad.networkmodel.GoogleResult
import com.appleby.breakingbad.networkmodel.Items

sealed class CharacterStates {
    data class NetworkSuccess(val imageResults: List<Items>) : CharacterStates()
    object NetworkFailure : CharacterStates()
}