package com.valsagnapp.dydapp.domain

data class AbilityScores(
    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int,
) {
    init {
        Ability.entries.forEach { ability ->
            require(scoreOf(ability) in MIN_SCORE..MAX_SCORE) {
                "$ability must be between $MIN_SCORE and $MAX_SCORE, was ${scoreOf(ability)}"
            }
        }
    }

    fun scoreOf(ability: Ability): Int = when (ability) {
        Ability.STRENGTH -> strength
        Ability.DEXTERITY -> dexterity
        Ability.CONSTITUTION -> constitution
        Ability.INTELLIGENCE -> intelligence
        Ability.WISDOM -> wisdom
        Ability.CHARISMA -> charisma
    }

    fun modifierOf(ability: Ability): Int = Math.floorDiv(scoreOf(ability) - AVERAGE_SCORE, 2)

    companion object {
        const val MIN_SCORE = 1
        const val MAX_SCORE = 30
        private const val AVERAGE_SCORE = 10
    }
}
