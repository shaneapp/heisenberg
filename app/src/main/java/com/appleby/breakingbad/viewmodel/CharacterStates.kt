package com.appleby.breakingbad.viewmodel

import com.appleby.breakingbad.networkmodel.GoogleResult
import com.appleby.breakingbad.networkmodel.Items

sealed class CharacterStates {
    object NetworkSuccess : CharacterStates()
    object NetworkFailure : CharacterStates()
}