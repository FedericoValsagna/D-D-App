package com.valsagnapp.dydapp.domain

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AbilityScoresTest {
    @ParameterizedTest
    @CsvSource("1, -5", "8, -1", "9, -1", "10, 0", "11, 0", "12, 1", "15, 2", "20, 5", "30, 10")
    fun `modifier is half the distance from 10 rounded down`(score: Int, expectedModifier: Int) {
        assertEquals(expectedModifier, abilityScores(strength = score).modifierOf(Ability.STRENGTH))
    }

    @Test
    fun `each ability reads its own score`() {
        val scores = AbilityScores(
            strength = 1,
            dexterity = 2,
            constitution = 3,
            intelligence = 4,
            wisdom = 5,
            charisma = 6,
        )

        assertEquals(listOf(1, 2, 3, 4, 5, 6), Ability.entries.map { scores.scoreOf(it) })
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 31])
    fun `rejects scores out of range`(score: Int) {
        assertFailsWith<IllegalArgumentException> { abilityScores(charisma = score) }
    }

    @ParameterizedTest
    @EnumSource(Ability::class)
    fun `validates every ability`(ability: Ability) {
        val scores = Ability.entries.associateWith { if (it == ability) 0 else 10 }

        assertFailsWith<IllegalArgumentException> {
            AbilityScores(
                strength = scores.getValue(Ability.STRENGTH),
                dexterity = scores.getValue(Ability.DEXTERITY),
                constitution = scores.getValue(Ability.CONSTITUTION),
                intelligence = scores.getValue(Ability.INTELLIGENCE),
                wisdom = scores.getValue(Ability.WISDOM),
                charisma = scores.getValue(Ability.CHARISMA),
            )
        }
    }
}
