package com.appleby.breakingbad.model

import com.appleby.breakingbad.networkmodel.Character

object CharacterRepo {
    val characters = mutableListOf<Character>()
    var season = 0
}