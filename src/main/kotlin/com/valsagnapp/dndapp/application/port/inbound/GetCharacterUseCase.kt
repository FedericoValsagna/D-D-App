package com.valsagnapp.dndapp.application.port.inbound

import com.valsagnapp.dndapp.domain.Character
import com.valsagnapp.dndapp.domain.CharacterId

interface GetCharacterUseCase {
    fun get(id: CharacterId): Character
}
