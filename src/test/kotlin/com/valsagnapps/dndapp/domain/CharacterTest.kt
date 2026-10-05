package com.valsagnapps.dndapp.domain

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CharacterTest {
    @ParameterizedTest
    @CsvSource("1, 2", "4, 2", "5, 3", "8, 3", "9, 4", "13, 5", "17, 6", "20, 6")
    fun `proficiency bonus grows every four levels`(level: Int, expectedBonus: Int) {
        assertEquals(expectedBonus, character(level = level).proficiencyBonus)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 21])
    fun `rejects levels out of range`(level: Int) {
        assertFailsWith<IllegalArgumentException> { character(level = level) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "   "])
    fun `rejects blank names`(name: String) {
        assertFailsWith<IllegalArgumentException> { character(name = name) }
    }

    @Test
    fun `rejects names that are too long`() {
        assertFailsWith<IllegalArgumentException> { character(name = "a".repeat(Character.MAX_NAME_LENGTH + 1)) }
    }

    @Test
    fun `skill bonus is the ability modifier without proficiency`() {
        val character = character(level = 5, abilityScores = abilityScores(dexterity = 16))

        assertEquals(3, character.skillBonus(Skill.STEALTH))
    }

    @Test
    fun `proficiency adds the proficiency bonus`() {
        val character = character(
            level = 5,
            abilityScores = abilityScores(dexterity = 16),
            skills = mapOf(Skill.STEALTH to Proficiency.PROFICIENT),
        )

        assertEquals(6, character.skillBonus(Skill.STEALTH))
    }

    @Test
    fun `expertise doubles the proficiency bonus`() {
        val character = character(
            level = 5,
            abilityScores = abilityScores(dexterity = 16),
            skills = mapOf(Skill.STEALTH to Proficiency.EXPERTISE),
        )

        assertEquals(9, character.skillBonus(Skill.STEALTH))
    }

    @Test
    fun `skill bonus can be negative`() {
        val character = character(abilityScores = abilityScores(charisma = 6))

        assertEquals(-2, character.skillBonus(Skill.PERSUASION))
    }

    @ParameterizedTest
    @EnumSource(Skill::class)
    fun `each skill uses its own ability modifier`(skill: Skill) {
        val scores = Ability.entries.associateWith { if (it == skill.ability) 20 else 10 }
        val character = character(
            abilityScores = AbilityScores(
                strength = scores.getValue(Ability.STRENGTH),
                dexterity = scores.getValue(Ability.DEXTERITY),
                constitution = scores.getValue(Ability.CONSTITUTION),
                intelligence = scores.getValue(Ability.INTELLIGENCE),
                wisdom = scores.getValue(Ability.WISDOM),
                charisma = scores.getValue(Ability.CHARISMA),
            ),
        )

        assertEquals(5, character.skillBonus(skill))
    }

    @Test
    fun `passive perception is 10 plus the perception bonus`() {
        val character = character(
            level = 9,
            abilityScores = abilityScores(wisdom = 14),
            skills = mapOf(Skill.PERCEPTION to Proficiency.PROFICIENT),
        )

        assertEquals(16, character.passivePerception)
    }

    @Test
    fun `passive perception without proficiency only uses wisdom`() {
        assertEquals(9, character(abilityScores = abilityScores(wisdom = 8)).passivePerception)
    }
}
