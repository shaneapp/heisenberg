package com.appleby.breakingbad

sealed class CharacterStates {
    object Success : CharacterStates()
    object Fail : CharacterStates()
}