package com.valsagnapps.dndapp.application.port.outbound

import com.valsagnapps.dndapp.domain.Character
import com.valsagnapps.dndapp.domain.CharacterId

interface CharacterRepository {
    fun save(character: Character): Character

    fun findById(id: CharacterId): Character?

    fun findAll(): List<Character>
}
