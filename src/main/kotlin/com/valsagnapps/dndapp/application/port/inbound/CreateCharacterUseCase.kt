package com.valsagnapps.dndapp.application.port.inbound

import com.valsagnapps.dndapp.domain.AbilityScores
import com.valsagnapps.dndapp.domain.Character

interface CreateCharacterUseCase {
    fun create(command: CreateCharacterCommand): Character
}

data class CreateCharacterCommand(val name: String, val level: Int, val abilityScores: AbilityScores)
