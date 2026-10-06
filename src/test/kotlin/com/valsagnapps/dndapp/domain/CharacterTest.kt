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

    @Test
    fun `level is the sum of the class levels`() {
        val character = character(
            classes = listOf(ClassLevel(CharacterClass.FIGHTER, 5), ClassLevel(CharacterClass.WIZARD, 3)),
        )

        assertEquals(8, character.level)
        assertEquals(3, character.proficiencyBonus)
    }

    @Test
    fun `rejects characters without classes`() {
        assertFailsWith<IllegalArgumentException> { character(classes = emptyList()) }
    }

    @Test
    fun `rejects repeated classes`() {
        assertFailsWith<IllegalArgumentException> {
            character(classes = listOf(ClassLevel(CharacterClass.ROGUE, 2), ClassLevel(CharacterClass.ROGUE, 3)))
        }
    }

    @Test
    fun `rejects a total level above 20`() {
        assertFailsWith<IllegalArgumentException> {
            character(classes = listOf(ClassLevel(CharacterClass.FIGHTER, 15), ClassLevel(CharacterClass.ROGUE, 6)))
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 1000])
    fun `rejects max hit points out of range`(maxHitPoints: Int) {
        assertFailsWith<IllegalArgumentException> { character(maxHitPoints = maxHitPoints) }
    }

    @Test
    fun `hit dice come from the class`() {
        assertEquals(mapOf(8 to 15), character(level = 15, characterClass = CharacterClass.CLERIC).hitDice)
    }

    @Test
    fun `hit dice of a multiclass are grouped by die from largest to smallest`() {
        val character = character(
            classes = listOf(
                ClassLevel(CharacterClass.WIZARD, 2),
                ClassLevel(CharacterClass.PALADIN, 3),
                ClassLevel(CharacterClass.FIGHTER, 1),
            ),
        )

        assertEquals(listOf(10 to 4, 6 to 2), character.hitDice.toList())
    }

    @Test
    fun `saving throws of the class are proficient`() {
        val character =
            character(level = 5, characterClass = CharacterClass.CLERIC, abilityScores = abilityScores(wisdom = 16))

        assertEquals(Proficiency.PROFICIENT, character.savingThrowProficiency(Ability.WISDOM))
        assertEquals(Proficiency.PROFICIENT, character.savingThrowProficiency(Ability.CHARISMA))
        assertEquals(Proficiency.NONE, character.savingThrowProficiency(Ability.STRENGTH))
        assertEquals(6, character.savingThrowBonus(Ability.WISDOM))
    }

    @Test
    fun `saving throw without proficiency is the ability modifier`() {
        val character =
            character(level = 5, characterClass = CharacterClass.CLERIC, abilityScores = abilityScores(strength = 8))

        assertEquals(-1, character.savingThrowBonus(Ability.STRENGTH))
    }

    @Test
    fun `only the starting class gives saving throw proficiencies`() {
        val character = character(
            classes = listOf(ClassLevel(CharacterClass.RANGER, 5), ClassLevel(CharacterClass.ROGUE, 2)),
        )

        assertEquals(Proficiency.PROFICIENT, character.savingThrowProficiency(Ability.DEXTERITY))
        assertEquals(Proficiency.PROFICIENT, character.savingThrowProficiency(Ability.STRENGTH))
        assertEquals(Proficiency.NONE, character.savingThrowProficiency(Ability.INTELLIGENCE))
    }

    @Test
    fun `a single class grants its full proficiencies`() {
        val rogue = character(characterClass = CharacterClass.ROGUE)

        assertEquals(CharacterClass.ROGUE.proficiencies.starting, rogue.proficiencies)
        assertEquals(listOf(CharacterClass.ROGUE.proficiencies.startingSkills), rogue.skillChoices)
    }

    @Test
    fun `multiclassing adds the reduced proficiencies of the other classes`() {
        val wizardFighter = character(
            classes = listOf(ClassLevel(CharacterClass.WIZARD, 3), ClassLevel(CharacterClass.FIGHTER, 2)),
        )

        assertEquals(
            setOf(ArmorProficiency.LIGHT, ArmorProficiency.MEDIUM, ArmorProficiency.SHIELDS),
            wizardFighter.proficiencies.armor,
        )
        assertEquals(setOf(WeaponProficiency.SIMPLE, WeaponProficiency.MARTIAL), wizardFighter.proficiencies.weapons)
    }

    @Test
    fun `the starting class decides which proficiencies are full`() {
        val fighterWizard = character(
            classes = listOf(ClassLevel(CharacterClass.FIGHTER, 2), ClassLevel(CharacterClass.WIZARD, 3)),
        )

        assertEquals(CharacterClass.FIGHTER.proficiencies.starting, fighterWizard.proficiencies)
    }

    @Test
    fun `each class offers its skills, reduced after the first`() {
        val rangerRogue = character(
            classes = listOf(ClassLevel(CharacterClass.RANGER, 5), ClassLevel(CharacterClass.ROGUE, 2)),
        )

        assertEquals(listOf(3, 1), rangerRogue.skillChoices.map { it.count })
        assertEquals(CharacterClass.ROGUE.proficiencies.startingSkills.options, rangerRogue.skillChoices[1].options)
    }

    @Test
    fun `multiclassing into a class without skills offers none`() {
        val clericWizard = character(
            classes = listOf(ClassLevel(CharacterClass.CLERIC, 5), ClassLevel(CharacterClass.WIZARD, 1)),
        )

        assertEquals(SkillChoice.NONE, clericWizard.skillChoices[1])
    }
}
