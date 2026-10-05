package com.valsagnapps.dndapp.domain

data class ClassLevel(val characterClass: CharacterClass, val level: Int) {
    init {
        require(level in Character.MIN_LEVEL..Character.MAX_LEVEL) {
            "$characterClass level must be between ${Character.MIN_LEVEL} and ${Character.MAX_LEVEL}, was $level"
        }
    }
}
