package com.valsagnapp.dndapp.application.port.outbound

import com.valsagnapp.dndapp.domain.Character
import com.valsagnapp.dndapp.domain.CharacterId

interface CharacterRepository {
    fun save(character: Character): Character

    fun findById(id: CharacterId): Character?
}
