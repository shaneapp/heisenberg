package com.appleby.breakingbad

sealed class CharacterStates {
    data class NetworkSuccess(val characters: List<Character>, val season: Int) : CharacterStates()
    object NetworkFailure : CharacterStates()
    data class SeasonFilter(val season: Int) : CharacterStates()
}