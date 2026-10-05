package com.valsagnapps.dndapp.application.service

import com.valsagnapps.dndapp.application.port.outbound.CharacterRepository
import com.valsagnapps.dndapp.domain.Character
import com.valsagnapps.dndapp.domain.CharacterId

class InMemoryCharacterRepository : CharacterRepository {
    private val characters = mutableMapOf<CharacterId, Character>()

    override fun save(character: Character): Character {
        characters[character.id] = character
        return character
    }

    override fun findById(id: CharacterId): Character? = characters[id]

    override fun findAll(): List<Character> = characters.values.toList()
}
