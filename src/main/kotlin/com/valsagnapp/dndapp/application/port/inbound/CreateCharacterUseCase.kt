package com.valsagnapp.dndapp.application.port.inbound

import com.valsagnapp.dndapp.domain.AbilityScores
import com.valsagnapp.dndapp.domain.Character

interface CreateCharacterUseCase {
    fun create(command: CreateCharacterCommand): Character
}

data class CreateCharacterCommand(val name: String, val level: Int, val abilityScores: AbilityScores)
