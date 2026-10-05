package com.valsagnapps.dndapp.application.port.inbound

import com.valsagnapps.dndapp.domain.AbilityScores
import com.valsagnapps.dndapp.domain.Character
import com.valsagnapps.dndapp.domain.SkillProficiencies

interface CreateCharacterUseCase {
    fun create(command: CreateCharacterCommand): Character
}

data class CreateCharacterCommand(
    val name: String,
    val level: Int,
    val abilityScores: AbilityScores,
    val skillProficiencies: SkillProficiencies = SkillProficiencies.NONE,
)
