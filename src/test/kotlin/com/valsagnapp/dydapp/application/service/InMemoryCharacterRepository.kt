package com.valsagnapp.dydapp.application.service

import com.valsagnapp.dydapp.application.port.outbound.CharacterRepository
import com.valsagnapp.dydapp.domain.Character
import com.valsagnapp.dydapp.domain.CharacterId

class InMemoryCharacterRepository : CharacterRepository {
    private val characters = mutableMapOf<CharacterId, Character>()

    override fun save(character: Character): Character {
        characters[character.id] = character
        return character
    }

    override fun findById(id: CharacterId): Character? = characters[id]
}
