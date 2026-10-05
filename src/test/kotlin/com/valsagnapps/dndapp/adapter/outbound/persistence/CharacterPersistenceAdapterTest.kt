package com.valsagnapps.dndapp.adapter.outbound.persistence

import com.valsagnapps.dndapp.TestcontainersConfiguration
import com.valsagnapps.dndapp.domain.CharacterId
import com.valsagnapps.dndapp.domain.abilityScores
import com.valsagnapps.dndapp.domain.character
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

    @Test
    fun `saves and loads a character`() {
        val character = character(
            name = "Ember",
            level = 7,
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
}
