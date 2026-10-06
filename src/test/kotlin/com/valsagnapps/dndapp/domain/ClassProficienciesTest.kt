package com.valsagnapps.dndapp.domain

import com.valsagnapps.dndapp.domain.ArmorProficiency.HEAVY
import com.valsagnapps.dndapp.domain.WeaponProficiency.MARTIAL
import com.valsagnapps.dndapp.domain.WeaponProficiency.SIMPLE
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ClassProficienciesTest {
    @ParameterizedTest
    @CsvSource(
        "BARBARIAN, 2, 6",
        "BARD, 3, 18",
        "CLERIC, 2, 5",
        "DRUID, 2, 8",
        "FIGHTER, 2, 8",
        "MONK, 2, 6",
        "PALADIN, 2, 6",
        "RANGER, 3, 8",
        "ROGUE, 4, 11",
        "SORCERER, 2, 6",
        "WARLOCK, 2, 7",
        "WIZARD, 2, 6",
    )
    fun `each class chooses its starting skills from its list`(
        characterClass: CharacterClass,
        count: Int,
        options: Int,
    ) {
        val skills = characterClass.proficiencies.startingSkills

        assertEquals(count, skills.count)
        assertEquals(options, skills.options.size)
    }

    @Test
    fun `only bards, rangers and rogues get a skill when multiclassing`() {
        val withSkill = CharacterClass.entries.filter { it.proficiencies.multiclassSkills.count > 0 }

        assertEquals(listOf(CharacterClass.BARD, CharacterClass.RANGER, CharacterClass.ROGUE), withSkill)
        CharacterClass.entries.forEach { assertTrue(it.proficiencies.multiclassSkills.count <= 1) }
    }

    @Test
    fun `multiclass skills come from the class list`() {
        val ranger = CharacterClass.RANGER.proficiencies

        assertEquals(ranger.startingSkills.options, ranger.multiclassSkills.options)
    }

    @ParameterizedTest
    @CsvSource(
        "BARBARIAN, 'LIGHT,MEDIUM,SHIELDS'",
        "BARD, LIGHT",
        "CLERIC, 'LIGHT,MEDIUM,SHIELDS'",
        "DRUID, 'LIGHT,MEDIUM,SHIELDS'",
        "FIGHTER, 'LIGHT,MEDIUM,HEAVY,SHIELDS'",
        "MONK, ''",
        "PALADIN, 'LIGHT,MEDIUM,HEAVY,SHIELDS'",
        "RANGER, 'LIGHT,MEDIUM,SHIELDS'",
        "ROGUE, LIGHT",
        "SORCERER, ''",
        "WARLOCK, LIGHT",
        "WIZARD, ''",
    )
    fun `each class has its starting armor`(characterClass: CharacterClass, armor: String?) {
        assertEquals(armorOf(armor), characterClass.proficiencies.starting.armor)
    }

    @ParameterizedTest
    @CsvSource(
        "BARBARIAN, SHIELDS",
        "BARD, LIGHT",
        "CLERIC, 'LIGHT,MEDIUM,SHIELDS'",
        "DRUID, 'LIGHT,MEDIUM,SHIELDS'",
        "FIGHTER, 'LIGHT,MEDIUM,SHIELDS'",
        "MONK, ''",
        "PALADIN, 'LIGHT,MEDIUM,SHIELDS'",
        "RANGER, 'LIGHT,MEDIUM,SHIELDS'",
        "ROGUE, LIGHT",
        "SORCERER, ''",
        "WARLOCK, LIGHT",
        "WIZARD, ''",
    )
    fun `multiclassing grants reduced armor`(characterClass: CharacterClass, armor: String?) {
        assertEquals(armorOf(armor), characterClass.proficiencies.multiclass.armor)
    }

    @Test
    fun `martial classes start with simple and martial weapons`() {
        listOf(CharacterClass.BARBARIAN, CharacterClass.FIGHTER, CharacterClass.PALADIN, CharacterClass.RANGER)
            .forEach { assertEquals(setOf(SIMPLE, MARTIAL), it.proficiencies.starting.weapons) }
    }

    @Test
    fun `rogues start with thieves tools and keep them when multiclassing`() {
        val rogue = CharacterClass.ROGUE.proficiencies

        assertEquals(setOf(ToolProficiency.THIEVES_TOOLS), rogue.starting.tools)
        assertEquals(setOf(ToolProficiency.THIEVES_TOOLS), rogue.multiclass.tools)
    }

    @Test
    fun `bards choose three instruments, or one when multiclassing`() {
        val bard = CharacterClass.BARD.proficiencies

        assertEquals(listOf(ToolChoice(3, setOf(ToolCategory.MUSICAL_INSTRUMENT))), bard.starting.toolChoices)
        assertEquals(listOf(ToolChoice(1, setOf(ToolCategory.MUSICAL_INSTRUMENT))), bard.multiclass.toolChoices)
    }

    @Test
    fun `monks choose artisan tools or an instrument`() {
        val choice = ToolChoice(1, setOf(ToolCategory.ARTISANS_TOOLS, ToolCategory.MUSICAL_INSTRUMENT))

        assertEquals(listOf(choice), CharacterClass.MONK.proficiencies.starting.toolChoices)
    }

    @ParameterizedTest
    @EnumSource(CharacterClass::class)
    fun `multiclass proficiencies are a subset of the starting ones`(characterClass: CharacterClass) {
        val proficiencies = characterClass.proficiencies

        assertTrue(proficiencies.starting.armor.containsAll(proficiencies.multiclass.armor))
        assertTrue(proficiencies.starting.weapons.containsAll(proficiencies.multiclass.weapons))
        assertTrue(proficiencies.starting.tools.containsAll(proficiencies.multiclass.tools))
    }

    @Test
    fun `heavy armor only comes from starting as a fighter or paladin`() {
        val withHeavy = CharacterClass.entries.filter { HEAVY in it.proficiencies.starting.armor }

        assertEquals(listOf(CharacterClass.FIGHTER, CharacterClass.PALADIN), withHeavy)
        CharacterClass.entries.forEach { assertTrue(HEAVY !in it.proficiencies.multiclass.armor) }
    }

    private fun armorOf(names: String?): Set<ArmorProficiency> =
        names.orEmpty().split(",").filter { it.isNotBlank() }.map { ArmorProficiency.valueOf(it.trim()) }.toSet()
}
