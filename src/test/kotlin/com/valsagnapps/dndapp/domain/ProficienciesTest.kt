package com.valsagnapps.dndapp.domain

import com.valsagnapps.dndapp.domain.WeaponProficiency.DAGGER
import com.valsagnapps.dndapp.domain.WeaponProficiency.LONGSWORD
import com.valsagnapps.dndapp.domain.WeaponProficiency.MARTIAL
import com.valsagnapps.dndapp.domain.WeaponProficiency.QUARTERSTAFF
import com.valsagnapps.dndapp.domain.WeaponProficiency.SCIMITAR
import com.valsagnapps.dndapp.domain.WeaponProficiency.SIMPLE
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ProficienciesTest {
    @Test
    fun `adding proficiencies joins armor, weapons and tools`() {
        val total = Proficiencies(armor = setOf(ArmorProficiency.LIGHT), weapons = setOf(DAGGER)) +
            Proficiencies(
                armor = setOf(ArmorProficiency.SHIELDS),
                weapons = setOf(LONGSWORD),
                tools = setOf(ToolProficiency.THIEVES_TOOLS),
            )

        assertEquals(setOf(ArmorProficiency.LIGHT, ArmorProficiency.SHIELDS), total.armor)
        assertEquals(setOf(DAGGER, LONGSWORD), total.weapons)
        assertEquals(setOf(ToolProficiency.THIEVES_TOOLS), total.tools)
    }

    @Test
    fun `a whole category absorbs its loose weapons`() {
        val total = Proficiencies(weapons = setOf(DAGGER, QUARTERSTAFF, SCIMITAR)) +
            Proficiencies(weapons = setOf(SIMPLE))

        assertEquals(setOf(SIMPLE, SCIMITAR), total.weapons)
    }

    @Test
    fun `simple and martial absorb every loose weapon`() {
        val total = Proficiencies(weapons = setOf(DAGGER, SCIMITAR)) + Proficiencies(weapons = setOf(SIMPLE, MARTIAL))

        assertEquals(setOf(SIMPLE, MARTIAL), total.weapons)
    }

    @Test
    fun `tool choices add up`() {
        val instrument = ToolChoice(1, setOf(ToolCategory.MUSICAL_INSTRUMENT))

        val total = Proficiencies(toolChoices = listOf(instrument)) + Proficiencies(toolChoices = listOf(instrument))

        assertEquals(listOf(instrument, instrument), total.toolChoices)
    }

    @Test
    fun `rejects empty tool choices`() {
        assertFailsWith<IllegalArgumentException> { ToolChoice(0, setOf(ToolCategory.ARTISANS_TOOLS)) }
        assertFailsWith<IllegalArgumentException> { ToolChoice(1, emptySet()) }
    }

    @Test
    fun `a skill choice cannot ask for more skills than it offers`() {
        assertFailsWith<IllegalArgumentException> { SkillChoice(3, setOf(Skill.ARCANA, Skill.HISTORY)) }
        assertFailsWith<IllegalArgumentException> { SkillChoice(-1, emptySet()) }
    }

    @Test
    fun `any skill choice offers all eighteen skills`() {
        assertEquals(18, SkillChoice.any(1).options.size)
    }
}
