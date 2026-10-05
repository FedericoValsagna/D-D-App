package com.valsagnapps.dndapp.domain

fun abilityScores(
    strength: Int = 10,
    dexterity: Int = 10,
    constitution: Int = 10,
    intelligence: Int = 10,
    wisdom: Int = 10,
    charisma: Int = 10,
) = AbilityScores(strength, dexterity, constitution, intelligence, wisdom, charisma)

fun character(
    id: CharacterId = CharacterId.new(),
    name: String = "Tordek",
    level: Int = 1,
    abilityScores: AbilityScores = abilityScores(),
) = Character(id, name, level, abilityScores)
