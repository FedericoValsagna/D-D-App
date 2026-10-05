package com.valsagnapps.dndapp.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class SkillProficienciesTest {
    @Test
    fun `skills not listed have no proficiency`() {
        val proficiencies = SkillProficiencies.of(mapOf(Skill.ARCANA to Proficiency.EXPERTISE))

        assertEquals(Proficiency.EXPERTISE, proficiencies.of(Skill.ARCANA))
        assertEquals(Proficiency.NONE, proficiencies.of(Skill.HISTORY))
    }

    @Test
    fun `explicit NONE is the same as not listing the skill`() {
        val explicit = SkillProficiencies.of(
            mapOf(Skill.ARCANA to Proficiency.NONE, Skill.HISTORY to Proficiency.PROFICIENT),
        )

        assertEquals(SkillProficiencies.of(mapOf(Skill.HISTORY to Proficiency.PROFICIENT)), explicit)
        assertEquals(mapOf(Skill.HISTORY to Proficiency.PROFICIENT), explicit.toMap())
        assertEquals(SkillProficiencies.NONE, SkillProficiencies.of(mapOf(Skill.ARCANA to Proficiency.NONE)))
    }

    @Test
    fun `different proficiencies are not equal`() {
        assertNotEquals(
            SkillProficiencies.of(mapOf(Skill.ARCANA to Proficiency.PROFICIENT)),
            SkillProficiencies.of(mapOf(Skill.ARCANA to Proficiency.EXPERTISE)),
        )
        assertNotEquals<Any>(SkillProficiencies.NONE, "NONE")
    }

    @Test
    fun `equal proficiencies have the same hash code and readable text`() {
        val a = SkillProficiencies.of(mapOf(Skill.ARCANA to Proficiency.PROFICIENT))
        val b = SkillProficiencies.of(mapOf(Skill.ARCANA to Proficiency.PROFICIENT))

        assertEquals(a.hashCode(), b.hashCode())
        assertEquals("SkillProficiencies({ARCANA=PROFICIENT})", a.toString())
    }
}
