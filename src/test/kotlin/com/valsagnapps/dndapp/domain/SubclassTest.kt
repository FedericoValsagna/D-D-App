package com.valsagnapps.dndapp.domain

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SubclassTest {
    @Test
    fun `the PHB has forty subclasses`() {
        assertEquals(40, Subclass.entries.size)
        assertEquals(setOf(Source.PHB), Subclass.entries.map { it.source }.toSet())
    }

    @ParameterizedTest
    @CsvSource(
        "BARBARIAN, 3, 2",
        "BARD, 3, 2",
        "CLERIC, 1, 7",
        "DRUID, 2, 2",
        "FIGHTER, 3, 3",
        "MONK, 3, 3",
        "PALADIN, 3, 3",
        "RANGER, 3, 2",
        "ROGUE, 3, 3",
        "SORCERER, 1, 2",
        "WARLOCK, 1, 3",
        "WIZARD, 2, 8",
    )
    fun `each class chooses its subclass at its level`(
        characterClass: CharacterClass,
        subclassLevel: Int,
        subclassCount: Int,
    ) {
        assertEquals(subclassLevel, characterClass.subclassLevel)
        assertEquals(subclassCount, characterClass.subclasses.size)
    }

    @ParameterizedTest
    @EnumSource(Subclass::class)
    fun `a subclass can be chosen from its class subclass level`(subclass: Subclass) {
        val characterClass = subclass.characterClass

        assertEquals(subclass, ClassLevel(characterClass, characterClass.subclassLevel, subclass).subclass)
    }

    @Test
    fun `a class level has no subclass by default`() {
        assertNull(ClassLevel(CharacterClass.FIGHTER, 5).subclass)
    }

    @Test
    fun `rejects a subclass of another class`() {
        assertFailsWith<IllegalArgumentException> { ClassLevel(CharacterClass.FIGHTER, 5, Subclass.LIFE) }
    }

    @Test
    fun `rejects a subclass below the level to choose it`() {
        assertFailsWith<IllegalArgumentException> { ClassLevel(CharacterClass.FIGHTER, 2, Subclass.CHAMPION) }
    }

    @Test
    fun `a class level can have a subclass from the subclass level`() {
        assertFalse(ClassLevel(CharacterClass.WIZARD, 1).canHaveSubclass)
        assertTrue(ClassLevel(CharacterClass.WIZARD, 2).canHaveSubclass)
    }
}
