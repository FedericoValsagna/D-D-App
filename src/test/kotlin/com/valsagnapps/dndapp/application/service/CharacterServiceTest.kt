package com.valsagnapps.dndapp.application.service

import com.valsagnapps.dndapp.domain.CharacterClass
import com.valsagnapps.dndapp.domain.CharacterId
import com.valsagnapps.dndapp.domain.CharacterNotFoundException
import com.valsagnapps.dndapp.domain.ClassLevel
import com.valsagnapps.dndapp.domain.Proficiency
import com.valsagnapps.dndapp.domain.Skill
import com.valsagnapps.dndapp.domain.SkillProficiencies
import com.valsagnapps.dndapp.domain.Subclass
import com.valsagnapps.dndapp.domain.abilityScores
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CharacterServiceTest {
    private val repository = InMemoryCharacterRepository()
    private val service = CharacterService(repository)

    @Test
    fun `creates and persists a character`() {
        val command = createCommand(
            name = "Lidda",
            level = 3,
            abilityScores = abilityScores(dexterity = 16),
            characterClass = CharacterClass.ROGUE,
            maxHitPoints = 20,
        )

        val created = service.create(command)

        assertEquals("Lidda", created.name)
        assertEquals(3, created.level)
        assertEquals(listOf(ClassLevel(CharacterClass.ROGUE, 3)), created.classes)
        assertEquals(20, created.maxHitPoints)
        assertEquals(16, created.abilityScores.dexterity)
        assertEquals(created, repository.findById(created.id))
    }

    @Test
    fun `gets an existing character`() {
        val created = service.create(createCommand("Mialee"))

        assertEquals(created, service.get(created.id))
    }

    @Test
    fun `lists characters sorted by name ignoring case`() {
        listOf("Mialee", "ember", "Tordek").forEach { service.create(createCommand(it)) }

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

        val created = service.create(createCommand("Lidda", skillProficiencies = skills))

        assertEquals(skills, repository.findById(created.id)?.skillProficiencies)
    }

    @Test
    fun `replaces the skill proficiencies of a character`() {
        val created = service.create(
            createCommand(
                "Lidda",
                skillProficiencies = SkillProficiencies.of(
                    mapOf(
                        Skill.STEALTH to Proficiency.PROFICIENT,
                    ),
                ),
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

    @Test
    fun `replaces the classes of a character`() {
        val created = service.create(createCommand("Jozan", level = 4))
        val newClasses = listOf(ClassLevel(CharacterClass.CLERIC, 3), ClassLevel(CharacterClass.FIGHTER, 1))

        val updated = service.updateClasses(created.id, newClasses)

        assertEquals(newClasses, updated.classes)
        assertEquals(created.copy(classes = newClasses), repository.findById(created.id))
    }

    @Test
    fun `keeps the subclass of the classes that stay`() {
        val created = service.create(createCommand("Jozan", characterClass = CharacterClass.CLERIC, level = 4))
        service.updateSubclass(created.id, CharacterClass.CLERIC, Subclass.LIFE)

        val updated = service.updateClasses(created.id, listOf(ClassLevel(CharacterClass.CLERIC, 5)))

        assertEquals(ClassLevel(CharacterClass.CLERIC, 5, Subclass.LIFE), updated.classes.single())
    }

    @Test
    fun `chooses the subclass of a class`() {
        val created = service.create(createCommand("Jozan", characterClass = CharacterClass.CLERIC, level = 4))

        val updated = service.updateSubclass(created.id, CharacterClass.CLERIC, Subclass.LIFE)

        assertEquals(Subclass.LIFE, updated.classes.single().subclass)
        assertEquals(updated, repository.findById(created.id))
    }

    @Test
    fun `fails to update the subclass when the character does not exist`() {
        assertFailsWith<CharacterNotFoundException> {
            service.updateSubclass(CharacterId.new(), CharacterClass.CLERIC, Subclass.LIFE)
        }
    }

    @Test
    fun `fails to update classes when the character does not exist`() {
        assertFailsWith<CharacterNotFoundException> {
            service.updateClasses(CharacterId.new(), listOf(ClassLevel(CharacterClass.BARD, 1)))
        }
    }

    @Test
    fun `updates the max hit points of a character`() {
        val created = service.create(createCommand("Krusk", maxHitPoints = 12))

        val updated = service.updateMaxHitPoints(created.id, 45)

        assertEquals(45, updated.maxHitPoints)
        assertEquals(created.copy(maxHitPoints = 45), repository.findById(created.id))
    }

    @Test
    fun `fails to update hit points when the character does not exist`() {
        assertFailsWith<CharacterNotFoundException> { service.updateMaxHitPoints(CharacterId.new(), 10) }
    }
}
