package com.valsagnapps.dndapp.adapter.outbound.persistence

import com.valsagnapps.dndapp.application.port.outbound.CharacterRepository
import com.valsagnapps.dndapp.domain.AbilityScores
import com.valsagnapps.dndapp.domain.Character
import com.valsagnapps.dndapp.domain.CharacterId
import com.valsagnapps.dndapp.domain.SkillProficiencies
import org.springframework.stereotype.Component

@Component
class CharacterPersistenceAdapter(private val jpaRepository: CharacterJpaRepository) : CharacterRepository {
    override fun save(character: Character): Character = jpaRepository.save(character.toEntity()).toDomain()

    override fun findById(id: CharacterId): Character? = jpaRepository.findById(id.value).orElse(null)?.toDomain()

    override fun findAll(): List<Character> = jpaRepository.findAll().map { it.toDomain() }
}

private fun Character.toEntity() = CharacterEntity(
    id = id.value,
    name = name,
    level = level,
    strength = abilityScores.strength,
    dexterity = abilityScores.dexterity,
    constitution = abilityScores.constitution,
    intelligence = abilityScores.intelligence,
    wisdom = abilityScores.wisdom,
    charisma = abilityScores.charisma,
    skills = skillProficiencies.toMap().toMutableMap(),
)

private fun CharacterEntity.toDomain() = Character(
    id = CharacterId(id),
    name = name,
    level = level,
    abilityScores = AbilityScores(strength, dexterity, constitution, intelligence, wisdom, charisma),
    skillProficiencies = SkillProficiencies.of(skills),
)
