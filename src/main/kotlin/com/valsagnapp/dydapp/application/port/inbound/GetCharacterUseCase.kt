package com.valsagnapp.dydapp.application.port.inbound

import com.valsagnapp.dydapp.domain.Character
import com.valsagnapp.dydapp.domain.CharacterId

interface GetCharacterUseCase {
    fun get(id: CharacterId): Character
}
