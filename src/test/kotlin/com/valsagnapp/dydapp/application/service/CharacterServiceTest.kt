package com.valsagnapp.dydapp.application.service

import com.valsagnapp.dydapp.application.port.inbound.CreateCharacterCommand
import com.valsagnapp.dydapp.domain.CharacterId
import com.valsagnapp.dydapp.domain.CharacterNotFoundException
import com.valsagnapp.dydapp.domain.abilityScores
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
    fun `fails when the character does not exist`() {
        assertFailsWith<CharacterNotFoundException> { service.get(CharacterId.new()) }
    }
}
