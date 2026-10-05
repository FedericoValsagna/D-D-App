package com.valsagnapp.dydapp.adapter.outbound.persistence

import com.valsagnapp.dydapp.application.port.outbound.CharacterRepository
import com.valsagnapp.dydapp.domain.AbilityScores
import com.valsagnapp.dydapp.domain.Character
import com.valsagnapp.dydapp.domain.CharacterId
import org.springframework.stereotype.Component

@Component
class CharacterPersistenceAdapter(private val jpaRepository: CharacterJpaRepository) : CharacterRepository {
    override fun save(character: Character): Character = jpaRepository.save(character.toEntity()).toDomain()

    override fun findById(id: CharacterId): Character? = jpaRepository.findById(id.value).orElse(null)?.toDomain()
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
)

private fun CharacterEntity.toDomain() = Character(
    id = CharacterId(id),
    name = name,
    level = level,
    abilityScores = AbilityScores(strength, dexterity, constitution, intelligence, wisdom, charisma),
)
