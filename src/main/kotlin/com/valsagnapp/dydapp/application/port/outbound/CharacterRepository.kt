package com.valsagnapp.dydapp.application.port.outbound

import com.valsagnapp.dydapp.domain.Character
import com.valsagnapp.dydapp.domain.CharacterId

interface CharacterRepository {
    fun save(character: Character): Character

    fun findById(id: CharacterId): Character?
}
