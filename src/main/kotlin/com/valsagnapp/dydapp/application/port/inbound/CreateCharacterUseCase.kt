package com.valsagnapp.dydapp.application.port.inbound

import com.valsagnapp.dydapp.domain.AbilityScores
import com.valsagnapp.dydapp.domain.Character

interface CreateCharacterUseCase {
    fun create(command: CreateCharacterCommand): Character
}

data class CreateCharacterCommand(val name: String, val level: Int, val abilityScores: AbilityScores)
