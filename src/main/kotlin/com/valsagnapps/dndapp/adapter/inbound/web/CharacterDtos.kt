package com.valsagnapps.dndapp.adapter.inbound.web

import com.valsagnapps.dndapp.application.port.inbound.CreateCharacterCommand
import com.valsagnapps.dndapp.domain.Ability
import com.valsagnapps.dndapp.domain.AbilityScores
import com.valsagnapps.dndapp.domain.Character
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

private const val MIN_LEVEL = Character.MIN_LEVEL.toLong()
private const val MAX_LEVEL = Character.MAX_LEVEL.toLong()
private const val MIN_SCORE = AbilityScores.MIN_SCORE.toLong()
private const val MAX_SCORE = AbilityScores.MAX_SCORE.toLong()

data class CreateCharacterRequest(
    @field:NotBlank @field:Size(max = Character.MAX_NAME_LENGTH) val name: String,
    @field:Min(MIN_LEVEL) @field:Max(MAX_LEVEL) val level: Int,
    @field:Valid val abilityScores: AbilityScoresDto,
) {
    fun toCommand() = CreateCharacterCommand(name = name, level = level, abilityScores = abilityScores.toDomain())
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

data class CharacterResponse(
    val id: UUID,
    val name: String,
    val level: Int,
    val proficiencyBonus: Int,
    val abilities: Map<Ability, AbilityResponse>,
) {
    companion object {
        fun from(character: Character) = CharacterResponse(
            id = character.id.value,
            name = character.name,
            level = character.level,
            proficiencyBonus = character.proficiencyBonus,
            abilities = Ability.entries.associateWith {
                AbilityResponse(
                    score = character.abilityScores.scoreOf(it),
                    modifier = character.abilityScores.modifierOf(it),
                )
            },
        )
    }
}
