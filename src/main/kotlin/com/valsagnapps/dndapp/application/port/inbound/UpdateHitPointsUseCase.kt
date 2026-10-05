package com.valsagnapps.dndapp.application.port.inbound

import com.valsagnapps.dndapp.domain.Character
import com.valsagnapps.dndapp.domain.CharacterId

interface UpdateHitPointsUseCase {
    fun updateMaxHitPoints(id: CharacterId, maxHitPoints: Int): Character
}
