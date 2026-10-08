package com.valsagnapps.dndapp.adapter.outbound.persistence

import com.valsagnapps.dndapp.TestcontainersConfiguration
import com.valsagnapps.dndapp.domain.CharacterClass
import com.valsagnapps.dndapp.domain.CharacterId
import com.valsagnapps.dndapp.domain.ClassLevel
import com.valsagnapps.dndapp.domain.Proficiency
import com.valsagnapps.dndapp.domain.Skill
import com.valsagnapps.dndapp.domain.SkillProficiencies
import com.valsagnapps.dndapp.domain.Subclass
import com.valsagnapps.dndapp.domain.abilityScores
import com.valsagnapps.dndapp.domain.character
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration::class, CharacterPersistenceAdapter::class)
class CharacterPersistenceAdapterTest {
    @Autowired
    lateinit var adapter: CharacterPersistenceAdapter

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun `saves and loads a character`() {
        val character = character(
            name = "Ember",
            level = 7,
            characterClass = CharacterClass.MONK,
            maxHitPoints = 52,
            abilityScores = abilityScores(
                strength = 8,
                dexterity = 18,
                constitution = 12,
                intelligence = 14,
                wisdom = 16,
                charisma = 9,
            ),
        )

        adapter.save(character)

        assertEquals(character, adapter.findById(character.id))
    }

    @Test
    fun `finds all characters`() {
        val characters = listOf(character(name = "Lidda"), character(name = "Krusk"))
        characters.forEach { adapter.save(it) }

        assertTrue(adapter.findAll().containsAll(characters))
    }

    @Test
    fun `returns null when the character does not exist`() {
        assertNull(adapter.findById(CharacterId.new()))
    }

    @Test
    fun `saves and loads skill proficiencies`() {
        val character = character(
            skills = mapOf(Skill.STEALTH to Proficiency.EXPERTISE, Skill.PERCEPTION to Proficiency.PROFICIENT),
        )

        adapter.save(character)
        entityManager.flush()
        entityManager.clear()

        assertEquals(character, adapter.findById(character.id))
    }

    @Test
    fun `replaces skill proficiencies when saving an existing character`() {
        val character = character(skills = mapOf(Skill.STEALTH to Proficiency.PROFICIENT))
        adapter.save(character)
        entityManager.flush()
        entityManager.clear()

        val updated = character.copy(
            skillProficiencies = SkillProficiencies.of(mapOf(Skill.ARCANA to Proficiency.EXPERTISE)),
        )
        adapter.save(updated)
        entityManager.flush()
        entityManager.clear()

        assertEquals(updated, adapter.findById(character.id))
        assertEquals(updated, adapter.findAll().single { it.id == character.id })
    }

    @Test
    fun `saves and loads classes in order`() {
        val character = character(
            classes = listOf(
                ClassLevel(CharacterClass.WIZARD, 3),
                ClassLevel(CharacterClass.FIGHTER, 2),
                ClassLevel(CharacterClass.CLERIC, 1),
            ),
        )

        adapter.save(character)
        entityManager.flush()
        entityManager.clear()

        assertEquals(character, adapter.findById(character.id))
    }

    @Test
    fun `replaces classes and hit points when saving an existing character`() {
        val character = character(
            classes = listOf(ClassLevel(CharacterClass.FIGHTER, 4), ClassLevel(CharacterClass.ROGUE, 2)),
        )
        adapter.save(character)
        entityManager.flush()
        entityManager.clear()

        val updated = character.copy(classes = listOf(ClassLevel(CharacterClass.RANGER, 5)), maxHitPoints = 42)
        adapter.save(updated)
        entityManager.flush()
        entityManager.clear()

        assertEquals(updated, adapter.findById(character.id))
    }

    @Test
    fun `saves and loads subclasses`() {
        val character = character(
            classes = listOf(ClassLevel(CharacterClass.CLERIC, 5, Subclass.LIFE), ClassLevel(CharacterClass.WIZARD, 1)),
        )

        adapter.save(character)
        entityManager.flush()
        entityManager.clear()

        assertEquals(character, adapter.findById(character.id))
    }

    @Test
    fun `removes the subclass when saving an existing character without it`() {
        val character = character(classes = listOf(ClassLevel(CharacterClass.FIGHTER, 3, Subclass.CHAMPION)))
        adapter.save(character)
        entityManager.flush()
        entityManager.clear()

        val updated = character.withSubclass(CharacterClass.FIGHTER, null)
        adapter.save(updated)
        entityManager.flush()
        entityManager.clear()

        assertEquals(updated, adapter.findById(character.id))
    }
}
