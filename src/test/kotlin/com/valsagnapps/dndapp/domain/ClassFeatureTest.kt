package com.valsagnapps.dndapp.domain

import com.valsagnapps.dndapp.domain.CharacterClass.CLERIC
import com.valsagnapps.dndapp.domain.CharacterClass.FIGHTER
import com.valsagnapps.dndapp.domain.CharacterClass.RANGER
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ClassFeatureTest {
    private val allFeatures =
        CharacterClass.entries.flatMap { it.features } + Subclass.entries.flatMap { it.features }

    @Test
    fun `feature ids are unique across classes and subclasses`() {
        assertEquals(allFeatures.size, allFeatures.map { it.id }.toSet().size)
    }

    @Test
    fun `every feature has a name, a summary and a level between 1 and 20`() {
        allFeatures.forEach {
            assertTrue(it.name.isNotBlank() && it.summary.isNotBlank(), it.id)
            assertTrue(it.level in Character.MIN_LEVEL..Character.MAX_LEVEL, it.id)
            assertTrue(it.srdText?.isNotBlank() ?: true, it.id)
        }
    }

    @ParameterizedTest
    @EnumSource(CharacterClass::class)
    fun `class features are sorted by level and prefixed with the class`(characterClass: CharacterClass) {
        val features = characterClass.features

        assertEquals(features.sortedBy { it.level }, features)
        assertTrue(features.all { it.id.startsWith("${characterClass}_") })
    }

    @ParameterizedTest
    @EnumSource(Subclass::class)
    fun `subclass features start at the subclass level and are prefixed with the subclass`(subclass: Subclass) {
        val features = subclass.features

        assertEquals(features.sortedBy { it.level }, features)
        assertTrue(features.all { it.level >= subclass.characterClass.subclassLevel })
        assertTrue(features.all { it.id.startsWith("${subclass}_") })
    }

    @Test
    fun `cleric and ranger have their features loaded`() {
        assertEquals(6, CLERIC.features.size)
        assertEquals(12, RANGER.features.size)
        assertEquals(6, Subclass.LIFE.features.size)
        assertEquals(4, Subclass.HUNTER.features.size)
    }

    @Test
    fun `classes without features loaded yet have none`() {
        assertEquals(emptyList(), FIGHTER.features)
        assertEquals(emptyList(), Subclass.CHAMPION.features)
    }

    @Test
    fun `a class level has the features of its class up to its level`() {
        val ids = ClassLevel(CLERIC, 4).features.map { it.id }

        assertEquals(
            listOf(
                "CLERIC_SPELLCASTING",
                "CLERIC_CHANNEL_DIVINITY",
                "CLERIC_TURN_UNDEAD",
                "CLERIC_ABILITY_SCORE_IMPROVEMENT",
            ),
            ids,
        )
    }

    @Test
    fun `a class level mixes in its subclass features by level`() {
        val ids = ClassLevel(CLERIC, 2, Subclass.LIFE).features.map { it.id }

        assertEquals(
            listOf(
                "CLERIC_SPELLCASTING",
                "LIFE_BONUS_PROFICIENCY",
                "LIFE_DISCIPLE_OF_LIFE",
                "CLERIC_CHANNEL_DIVINITY",
                "CLERIC_TURN_UNDEAD",
                "LIFE_PRESERVE_LIFE",
            ),
            ids,
        )
    }

    @Test
    fun `a class level at 20 has every feature of its class and subclass`() {
        val features = ClassLevel(RANGER, 20, Subclass.HUNTER).features

        assertEquals(RANGER.features.size + Subclass.HUNTER.features.size, features.size)
        assertEquals("RANGER_FOE_SLAYER", features.last().id)
    }

    @Test
    fun `features mark whether they come with the SRD text`() {
        assertTrue(allFeatures.all { it.source == Source.PHB })
        assertTrue(CLERIC.features.all { it.srdText != null })
    }
}
