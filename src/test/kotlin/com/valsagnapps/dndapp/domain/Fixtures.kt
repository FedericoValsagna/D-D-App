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
    characterClass: CharacterClass = CharacterClass.FIGHTER,
    classes: List<ClassLevel> = listOf(ClassLevel(characterClass, level)),
    abilityScores: AbilityScores = abilityScores(),
    maxHitPoints: Int = 10,
    skills: Map<Skill, Proficiency> = emptyMap(),
) = Character(id, name, classes, abilityScores, maxHitPoints, SkillProficiencies.of(skills))
