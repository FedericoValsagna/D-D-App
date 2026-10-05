package com.valsagnapps.dndapp.adapter.inbound.web

import com.fasterxml.jackson.annotation.JsonProperty
import com.valsagnapps.dndapp.application.port.inbound.CreateCharacterCommand
import com.valsagnapps.dndapp.domain.Ability
import com.valsagnapps.dndapp.domain.AbilityScores
import com.valsagnapps.dndapp.domain.Character
import com.valsagnapps.dndapp.domain.CharacterClass
import com.valsagnapps.dndapp.domain.ClassLevel
import com.valsagnapps.dndapp.domain.Proficiency
import com.valsagnapps.dndapp.domain.Skill
import com.valsagnapps.dndapp.domain.SkillProficiencies
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import java.util.UUID

private const val MIN_LEVEL = Character.MIN_LEVEL.toLong()
private const val MAX_LEVEL = Character.MAX_LEVEL.toLong()
private const val MIN_SCORE = AbilityScores.MIN_SCORE.toLong()
private const val MAX_SCORE = AbilityScores.MAX_SCORE.toLong()
private const val MIN_HIT_POINTS = Character.MIN_HIT_POINTS.toLong()
private const val MAX_HIT_POINTS = Character.MAX_HIT_POINTS.toLong()

data class CreateCharacterRequest(
    @field:NotBlank @field:Size(max = Character.MAX_NAME_LENGTH) val name: String,
    @field:NotEmpty @field:Valid val classes: List<ClassLevelDto>,
    @field:Valid val abilityScores: AbilityScoresDto,
    @field:Min(MIN_HIT_POINTS) @field:Max(MAX_HIT_POINTS) val maxHitPoints: Int,
    // Skills que no vienen quedan sin competencia (NONE).
    val skills: Map<Skill, Proficiency> = emptyMap(),
) {
    fun toCommand() = CreateCharacterCommand(
        name = name,
        classes = classes.map { it.toDomain() },
        abilityScores = abilityScores.toDomain(),
        maxHitPoints = maxHitPoints,
        skillProficiencies = SkillProficiencies.of(skills),
    )
}

// Reemplaza todas las competencias: las skills que no vienen quedan en NONE.
data class UpdateSkillsRequest(val skills: Map<Skill, Proficiency>) {
    fun toDomain() = SkillProficiencies.of(skills)
}

// Reemplaza todas las clases; la primera es la clase inicial (la que da las salvaciones).
data class UpdateClassesRequest(@field:NotEmpty @field:Valid val classes: List<ClassLevelDto>) {
    fun toDomain() = classes.map { it.toDomain() }
}

data class UpdateHitPointsRequest(@field:Min(MIN_HIT_POINTS) @field:Max(MAX_HIT_POINTS) val maxHitPoints: Int)

data class ClassLevelDto(
    @param:JsonProperty("class") @get:JsonProperty("class") val characterClass: CharacterClass,
    @field:Min(MIN_LEVEL) @field:Max(MAX_LEVEL) val level: Int,
) {
    fun toDomain() = ClassLevel(characterClass, level)
}

data class AbilityScoresDto(
    @field:Min(MIN_SCORE) @field:Max(MAX_SCORE) val strength: Int,
    @field:Min(MIN_SCORE) @field:Max(MAX_SCORE) val dexterity: Int,
    @field:Min(MIN_SCORE) @field:Max(MAX_SCORE) val constitution: Int,
    @field:Min(MIN_SCORE) @field:Max(MAX_SCORE) val intelligence: Int,
    @field:Min(MIN_SCORE) @field:Max(MAX_SCORE) val wisdom: Int,
    @field:Min(MIN_SCORE) @field:Max(MAX_SCORE) val charisma: Int,
) {
    fun toDomain() = AbilityScores(strength, dexterity, constitution, intelligence, wisdom, charisma)
}

data class AbilityResponse(val score: Int, val modifier: Int)

data class SkillResponse(val ability: Ability, val proficiency: Proficiency, val bonus: Int)

data class SavingThrowResponse(val proficiency: Proficiency, val bonus: Int)

data class ClassLevelResponse(
    @get:JsonProperty("class") val characterClass: CharacterClass,
    val level: Int,
    val hitDie: Int,
)

data class HitDiceResponse(val die: Int, val count: Int)

data class CharacterResponse(
    val id: UUID,
    val name: String,
    val level: Int,
    val classes: List<ClassLevelResponse>,
    val proficiencyBonus: Int,
    val maxHitPoints: Int,
    val hitDice: List<HitDiceResponse>,
    val abilities: Map<Ability, AbilityResponse>,
    val savingThrows: Map<Ability, SavingThrowResponse>,
    val skills: Map<Skill, SkillResponse>,
    val passivePerception: Int,
) {
    companion object {
        fun from(character: Character) = CharacterResponse(
            id = character.id.value,
            name = character.name,
            level = character.level,
            classes = character.classes.map {
                ClassLevelResponse(
                    characterClass = it.characterClass,
                    level = it.level,
                    hitDie = it.characterClass.hitDie,
                )
            },
            proficiencyBonus = character.proficiencyBonus,
            maxHitPoints = character.maxHitPoints,
            hitDice = character.hitDice.map { (die, count) -> HitDiceResponse(die, count) },
            abilities = Ability.entries.associateWith {
                AbilityResponse(
                    score = character.abilityScores.scoreOf(it),
                    modifier = character.abilityScores.modifierOf(it),
                )
            },
            savingThrows = Ability.entries.associateWith {
                SavingThrowResponse(
                    proficiency = character.savingThrowProficiency(it),
                    bonus = character.savingThrowBonus(it),
                )
            },
            skills = Skill.entries.associateWith {
                SkillResponse(
                    ability = it.ability,
                    proficiency = character.skillProficiencies.of(it),
                    bonus = character.skillBonus(it),
                )
            },
            passivePerception = character.passivePerception,
        )
    }
}
