package com.valsagnapps.dndapp.domain

// La subclase es opcional: puede no tener aunque ya tenga el nivel para elegirla.
data class ClassLevel(val characterClass: CharacterClass, val level: Int, val subclass: Subclass? = null) {
    init {
        require(level in Character.MIN_LEVEL..Character.MAX_LEVEL) {
            "$characterClass level must be between ${Character.MIN_LEVEL} and ${Character.MAX_LEVEL}, was $level"
        }
        if (subclass != null) {
            require(subclass.characterClass == characterClass) { "$subclass is not a $characterClass subclass" }
            require(canHaveSubclass) {
                "$characterClass chooses its subclass at level ${characterClass.subclassLevel}, was $level"
            }
        }
    }

    val canHaveSubclass: Boolean
        get() = level >= characterClass.subclassLevel
}
