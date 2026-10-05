package com.valsagnapps.dndapp.application.service

import com.valsagnapps.dndapp.application.port.inbound.CreateCharacterCommand
import com.valsagnapps.dndapp.domain.AbilityScores
import com.valsagnapps.dndapp.domain.CharacterClass
import com.valsagnapps.dndapp.domain.ClassLevel
import com.valsagnapps.dndapp.domain.SkillProficiencies
import com.valsagnapps.dndapp.domain.abilityScores

fun createCommand(
    name: String,
    level: Int = 1,
    abilityScores: AbilityScores = abilityScores(),
    characterClass: CharacterClass = CharacterClass.FIGHTER,
    maxHitPoints: Int = 10,
    skillProficiencies: SkillProficiencies = SkillProficiencies.NONE,
) = CreateCharacterCommand(
    name = name,
    classes = listOf(ClassLevel(characterClass, level)),
    abilityScores = abilityScores,
    maxHitPoints = maxHitPoints,
    skillProficiencies = skillProficiencies,
)
