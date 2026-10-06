package com.valsagnapps.dndapp.domain

import com.valsagnapps.dndapp.domain.ArmorProficiency.HEAVY
import com.valsagnapps.dndapp.domain.ArmorProficiency.LIGHT
import com.valsagnapps.dndapp.domain.ArmorProficiency.MEDIUM
import com.valsagnapps.dndapp.domain.ArmorProficiency.SHIELDS
import com.valsagnapps.dndapp.domain.Skill.ACROBATICS
import com.valsagnapps.dndapp.domain.Skill.ANIMAL_HANDLING
import com.valsagnapps.dndapp.domain.Skill.ARCANA
import com.valsagnapps.dndapp.domain.Skill.ATHLETICS
import com.valsagnapps.dndapp.domain.Skill.DECEPTION
import com.valsagnapps.dndapp.domain.Skill.HISTORY
import com.valsagnapps.dndapp.domain.Skill.INSIGHT
import com.valsagnapps.dndapp.domain.Skill.INTIMIDATION
import com.valsagnapps.dndapp.domain.Skill.INVESTIGATION
import com.valsagnapps.dndapp.domain.Skill.MEDICINE
import com.valsagnapps.dndapp.domain.Skill.NATURE
import com.valsagnapps.dndapp.domain.Skill.PERCEPTION
import com.valsagnapps.dndapp.domain.Skill.PERFORMANCE
import com.valsagnapps.dndapp.domain.Skill.PERSUASION
import com.valsagnapps.dndapp.domain.Skill.RELIGION
import com.valsagnapps.dndapp.domain.Skill.SLEIGHT_OF_HAND
import com.valsagnapps.dndapp.domain.Skill.STEALTH
import com.valsagnapps.dndapp.domain.Skill.SURVIVAL
import com.valsagnapps.dndapp.domain.ToolCategory.ARTISANS_TOOLS
import com.valsagnapps.dndapp.domain.ToolCategory.MUSICAL_INSTRUMENT
import com.valsagnapps.dndapp.domain.ToolProficiency.HERBALISM_KIT
import com.valsagnapps.dndapp.domain.ToolProficiency.THIEVES_TOOLS
import com.valsagnapps.dndapp.domain.WeaponProficiency.CLUB
import com.valsagnapps.dndapp.domain.WeaponProficiency.DAGGER
import com.valsagnapps.dndapp.domain.WeaponProficiency.DART
import com.valsagnapps.dndapp.domain.WeaponProficiency.HAND_CROSSBOW
import com.valsagnapps.dndapp.domain.WeaponProficiency.JAVELIN
import com.valsagnapps.dndapp.domain.WeaponProficiency.LIGHT_CROSSBOW
import com.valsagnapps.dndapp.domain.WeaponProficiency.LONGSWORD
import com.valsagnapps.dndapp.domain.WeaponProficiency.MACE
import com.valsagnapps.dndapp.domain.WeaponProficiency.MARTIAL
import com.valsagnapps.dndapp.domain.WeaponProficiency.QUARTERSTAFF
import com.valsagnapps.dndapp.domain.WeaponProficiency.RAPIER
import com.valsagnapps.dndapp.domain.WeaponProficiency.SCIMITAR
import com.valsagnapps.dndapp.domain.WeaponProficiency.SHORTSWORD
import com.valsagnapps.dndapp.domain.WeaponProficiency.SICKLE
import com.valsagnapps.dndapp.domain.WeaponProficiency.SIMPLE
import com.valsagnapps.dndapp.domain.WeaponProficiency.SLING
import com.valsagnapps.dndapp.domain.WeaponProficiency.SPEAR

// Competencias que da una clase: completas si es la clase inicial, reducidas si se suma por multiclase
// (tabla "Multiclassing Proficiencies" del PHB). Los druidas no usan armadura ni escudo de metal.
data class ClassProficiencies(
    val starting: Proficiencies,
    val startingSkills: SkillChoice,
    val multiclass: Proficiencies = Proficiencies.NONE,
    val multiclassSkills: SkillChoice = SkillChoice.NONE,
)

private val ALL_ARMOR = setOf(LIGHT, MEDIUM, HEAVY, SHIELDS)
private val NON_HEAVY_ARMOR = setOf(LIGHT, MEDIUM, SHIELDS)
private val ALL_WEAPONS = setOf(SIMPLE, MARTIAL)
private val FINESSE_WEAPONS = setOf(SIMPLE, HAND_CROSSBOW, LONGSWORD, RAPIER, SHORTSWORD)
private val ARCANE_WEAPONS = setOf(DAGGER, DART, SLING, QUARTERSTAFF, LIGHT_CROSSBOW)
private val DRUID_WEAPONS = setOf(CLUB, DAGGER, DART, JAVELIN, MACE, QUARTERSTAFF, SCIMITAR, SICKLE, SLING, SPEAR)
private val MARTIAL_MULTICLASS = Proficiencies(armor = NON_HEAVY_ARMOR, weapons = ALL_WEAPONS)

private fun choose(count: Int, vararg skills: Skill) = SkillChoice(count, skills.toSet())

private val RANGER_SKILLS =
    choose(3, ANIMAL_HANDLING, ATHLETICS, INSIGHT, INVESTIGATION, NATURE, PERCEPTION, STEALTH, SURVIVAL)
private val ROGUE_SKILLS = choose(
    4,
    ACROBATICS, ATHLETICS, DECEPTION, INSIGHT, INTIMIDATION, INVESTIGATION,
    PERCEPTION, PERFORMANCE, PERSUASION, SLEIGHT_OF_HAND, STEALTH,
)

@Suppress("MagicNumber")
internal val CLASS_PROFICIENCIES: Map<CharacterClass, ClassProficiencies> = mapOf(
    CharacterClass.BARBARIAN to ClassProficiencies(
        starting = Proficiencies(armor = NON_HEAVY_ARMOR, weapons = ALL_WEAPONS),
        startingSkills = choose(2, ANIMAL_HANDLING, ATHLETICS, INTIMIDATION, NATURE, PERCEPTION, SURVIVAL),
        multiclass = Proficiencies(armor = setOf(SHIELDS), weapons = ALL_WEAPONS),
    ),
    CharacterClass.BARD to ClassProficiencies(
        starting = Proficiencies(
            armor = setOf(LIGHT),
            weapons = FINESSE_WEAPONS,
            toolChoices = listOf(ToolChoice(3, setOf(MUSICAL_INSTRUMENT))),
        ),
        startingSkills = SkillChoice.any(3),
        multiclass = Proficiencies(
            armor = setOf(LIGHT),
            toolChoices = listOf(ToolChoice(1, setOf(MUSICAL_INSTRUMENT))),
        ),
        multiclassSkills = SkillChoice.any(1),
    ),
    CharacterClass.CLERIC to ClassProficiencies(
        starting = Proficiencies(armor = NON_HEAVY_ARMOR, weapons = setOf(SIMPLE)),
        startingSkills = choose(2, HISTORY, INSIGHT, MEDICINE, PERSUASION, RELIGION),
        multiclass = Proficiencies(armor = NON_HEAVY_ARMOR),
    ),
    CharacterClass.DRUID to ClassProficiencies(
        starting = Proficiencies(armor = NON_HEAVY_ARMOR, weapons = DRUID_WEAPONS, tools = setOf(HERBALISM_KIT)),
        startingSkills = choose(2, ARCANA, ANIMAL_HANDLING, INSIGHT, MEDICINE, NATURE, PERCEPTION, RELIGION, SURVIVAL),
        multiclass = Proficiencies(armor = NON_HEAVY_ARMOR),
    ),
    CharacterClass.FIGHTER to ClassProficiencies(
        starting = Proficiencies(armor = ALL_ARMOR, weapons = ALL_WEAPONS),
        startingSkills = choose(
            2,
            ACROBATICS, ANIMAL_HANDLING, ATHLETICS, HISTORY, INSIGHT, INTIMIDATION, PERCEPTION, SURVIVAL,
        ),
        multiclass = MARTIAL_MULTICLASS,
    ),
    CharacterClass.MONK to ClassProficiencies(
        starting = Proficiencies(
            weapons = setOf(SIMPLE, SHORTSWORD),
            toolChoices = listOf(ToolChoice(1, setOf(ARTISANS_TOOLS, MUSICAL_INSTRUMENT))),
        ),
        startingSkills = choose(2, ACROBATICS, ATHLETICS, HISTORY, INSIGHT, RELIGION, STEALTH),
        multiclass = Proficiencies(weapons = setOf(SIMPLE, SHORTSWORD)),
    ),
    CharacterClass.PALADIN to ClassProficiencies(
        starting = Proficiencies(armor = ALL_ARMOR, weapons = ALL_WEAPONS),
        startingSkills = choose(2, ATHLETICS, INSIGHT, INTIMIDATION, MEDICINE, PERSUASION, RELIGION),
        multiclass = MARTIAL_MULTICLASS,
    ),
    CharacterClass.RANGER to ClassProficiencies(
        starting = Proficiencies(armor = NON_HEAVY_ARMOR, weapons = ALL_WEAPONS),
        startingSkills = RANGER_SKILLS,
        multiclass = MARTIAL_MULTICLASS,
        multiclassSkills = RANGER_SKILLS.copy(count = 1),
    ),
    CharacterClass.ROGUE to ClassProficiencies(
        starting = Proficiencies(armor = setOf(LIGHT), weapons = FINESSE_WEAPONS, tools = setOf(THIEVES_TOOLS)),
        startingSkills = ROGUE_SKILLS,
        multiclass = Proficiencies(armor = setOf(LIGHT), tools = setOf(THIEVES_TOOLS)),
        multiclassSkills = ROGUE_SKILLS.copy(count = 1),
    ),
    CharacterClass.SORCERER to ClassProficiencies(
        starting = Proficiencies(weapons = ARCANE_WEAPONS),
        startingSkills = choose(2, ARCANA, DECEPTION, INSIGHT, INTIMIDATION, PERSUASION, RELIGION),
    ),
    CharacterClass.WARLOCK to ClassProficiencies(
        starting = Proficiencies(armor = setOf(LIGHT), weapons = setOf(SIMPLE)),
        startingSkills = choose(2, ARCANA, DECEPTION, HISTORY, INTIMIDATION, INVESTIGATION, NATURE, RELIGION),
        multiclass = Proficiencies(armor = setOf(LIGHT), weapons = setOf(SIMPLE)),
    ),
    CharacterClass.WIZARD to ClassProficiencies(
        starting = Proficiencies(weapons = ARCANE_WEAPONS),
        startingSkills = choose(2, ARCANA, HISTORY, INSIGHT, INVESTIGATION, MEDICINE, RELIGION),
    ),
)
