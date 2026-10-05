package com.valsagnapp.dydapp.domain

data class Character(val id: CharacterId, val name: String, val level: Int, val abilityScores: AbilityScores) {
    init {
        require(name.isNotBlank()) { "name must not be blank" }
        require(name.length <= MAX_NAME_LENGTH) { "name must have at most $MAX_NAME_LENGTH characters" }
        require(level in MIN_LEVEL..MAX_LEVEL) { "level must be between $MIN_LEVEL and $MAX_LEVEL, was $level" }
    }

    // +2 en niveles 1-4, +3 en 5-8, ... +6 en 17-20.
    val proficiencyBonus: Int
        get() = 2 + (level - 1) / 4

    companion object {
        const val MIN_LEVEL = 1
        const val MAX_LEVEL = 20
        const val MAX_NAME_LENGTH = 100
    }
}
