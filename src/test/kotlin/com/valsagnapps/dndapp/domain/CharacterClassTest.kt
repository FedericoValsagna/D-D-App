package com.valsagnapps.dndapp.domain

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CharacterClassTest {
    @Test
    fun `the PHB has twelve classes`() {
        assertEquals(12, CharacterClass.entries.size)
        assertEquals(setOf(Source.PHB), CharacterClass.entries.map { it.source }.toSet())
    }

    @ParameterizedTest
    @CsvSource(
        "BARBARIAN, 12, STRENGTH, CONSTITUTION",
        "BARD, 8, DEXTERITY, CHARISMA",
        "CLERIC, 8, WISDOM, CHARISMA",
        "DRUID, 8, INTELLIGENCE, WISDOM",
        "FIGHTER, 10, STRENGTH, CONSTITUTION",
        "MONK, 8, STRENGTH, DEXTERITY",
        "PALADIN, 10, WISDOM, CHARISMA",
        "RANGER, 10, STRENGTH, DEXTERITY",
        "ROGUE, 8, DEXTERITY, INTELLIGENCE",
        "SORCERER, 6, CONSTITUTION, CHARISMA",
        "WARLOCK, 8, WISDOM, CHARISMA",
        "WIZARD, 6, INTELLIGENCE, WISDOM",
    )
    fun `each class has its hit die and saving throws`(
        characterClass: CharacterClass,
        hitDie: Int,
        firstSave: Ability,
        secondSave: Ability,
    ) {
        assertEquals(hitDie, characterClass.hitDie)
        assertEquals(setOf(firstSave, secondSave), characterClass.savingThrows)
    }

    @ParameterizedTest
    @EnumSource(CharacterClass::class)
    fun `a class level can go from 1 to 20`(characterClass: CharacterClass) {
        assertEquals(20, ClassLevel(characterClass, 20).level)
        assertEquals(1, ClassLevel(characterClass, 1).level)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 21])
    fun `rejects class levels out of range`(level: Int) {
        assertFailsWith<IllegalArgumentException> { ClassLevel(CharacterClass.BARD, level) }
    }
}
