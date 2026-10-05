package com.valsagnapps.dndapp.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class SkillTest {
    @Test
    fun `there are 18 skills`() {
        assertEquals(18, Skill.entries.size)
    }

    @Test
    fun `skills are grouped by ability as in the rules`() {
        val byAbility = Skill.entries.groupBy({ it.ability }, { it.name }).mapValues { it.value.sorted() }

        assertEquals(
            mapOf(
                Ability.STRENGTH to listOf("ATHLETICS"),
                Ability.DEXTERITY to listOf("ACROBATICS", "SLEIGHT_OF_HAND", "STEALTH"),
                Ability.INTELLIGENCE to listOf("ARCANA", "HISTORY", "INVESTIGATION", "NATURE", "RELIGION"),
                Ability.WISDOM to listOf("ANIMAL_HANDLING", "INSIGHT", "MEDICINE", "PERCEPTION", "SURVIVAL"),
                Ability.CHARISMA to listOf("DECEPTION", "INTIMIDATION", "PERFORMANCE", "PERSUASION"),
            ),
            byAbility,
        )
    }
}
