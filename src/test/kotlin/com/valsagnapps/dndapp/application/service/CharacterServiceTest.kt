package com.valsagnapps.dndapp.application.service

import com.valsagnapps.dndapp.application.port.inbound.CreateCharacterCommand
import com.valsagnapps.dndapp.domain.CharacterId
import com.valsagnapps.dndapp.domain.CharacterNotFoundException
import com.valsagnapps.dndapp.domain.Proficiency
import com.valsagnapps.dndapp.domain.Skill
import com.valsagnapps.dndapp.domain.SkillProficiencies
import com.valsagnapps.dndapp.domain.abilityScores
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CharacterServiceTest {
    private val repository = InMemoryCharacterRepository()
    private val service = CharacterService(repository)

    @Test
    fun `creates and persists a character`() {
        val command = CreateCharacterCommand(name = "Lidda", level = 3, abilityScores = abilityScores(dexterity = 16))

        val created = service.create(command)

        assertEquals("Lidda", created.name)
        assertEquals(3, created.level)
        assertEquals(16, created.abilityScores.dexterity)
        assertEquals(created, repository.findById(created.id))
    }

    @Test
    fun `gets an existing character`() {
        val created = service.create(CreateCharacterCommand("Mialee", 1, abilityScores()))

        assertEquals(created, service.get(created.id))
    }

    @Test
    fun `lists characters sorted by name ignoring case`() {
        listOf("Mialee", "ember", "Tordek").forEach { service.create(CreateCharacterCommand(it, 1, abilityScores())) }

        assertEquals(listOf("ember", "Mialee", "Tordek"), service.list().map { it.name })
    }

    @Test
    fun `lists nothing when there are no characters`() {
        assertEquals(emptyList(), service.list())
    }

    @Test
    fun `fails when the character does not exist`() {
        assertFailsWith<CharacterNotFoundException> { service.get(CharacterId.new()) }
    }

    @Test
    fun `creates a character with skill proficiencies`() {
        val skills = SkillProficiencies.of(mapOf(Skill.STEALTH to Proficiency.EXPERTISE))

        val created = service.create(CreateCharacterCommand("Lidda", 1, abilityScores(), skills))

        assertEquals(skills, repository.findById(created.id)?.skillProficiencies)
    }

    @Test
    fun `replaces the skill proficiencies of a character`() {
        val created = service.create(
            CreateCharacterCommand(
                "Lidda",
                1,
                abilityScores(),
                SkillProficiencies.of(mapOf(Skill.STEALTH to Proficiency.PROFICIENT)),
            ),
        )
        val newSkills = SkillProficiencies.of(mapOf(Skill.ACROBATICS to Proficiency.EXPERTISE))

        val updated = service.updateSkills(created.id, newSkills)

        assertEquals(newSkills, updated.skillProficiencies)
        assertEquals(created.copy(skillProficiencies = newSkills), repository.findById(created.id))
    }

    @Test
    fun `fails to update skills when the character does not exist`() {
        assertFailsWith<CharacterNotFoundException> { service.updateSkills(CharacterId.new(), SkillProficiencies.NONE) }
    }
}
