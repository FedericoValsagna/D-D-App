package com.valsagnapps.dndapp.application.service

import com.valsagnapps.dndapp.application.port.inbound.CreateCharacterCommand
import com.valsagnapps.dndapp.application.port.inbound.CreateCharacterUseCase
import com.valsagnapps.dndapp.application.port.inbound.GetCharacterUseCase
import com.valsagnapps.dndapp.application.port.inbound.ListCharactersUseCase
import com.valsagnapps.dndapp.application.port.inbound.UpdateSkillsUseCase
import com.valsagnapps.dndapp.application.port.outbound.CharacterRepository
import com.valsagnapps.dndapp.domain.Character
import com.valsagnapps.dndapp.domain.CharacterId
import com.valsagnapps.dndapp.domain.CharacterNotFoundException
import com.valsagnapps.dndapp.domain.SkillProficiencies

class CharacterService(private val characterRepository: CharacterRepository) :
    CreateCharacterUseCase,
    GetCharacterUseCase,
    ListCharactersUseCase,
    UpdateSkillsUseCase {
    override fun create(command: CreateCharacterCommand): Character = characterRepository.save(
        Character(
            id = CharacterId.new(),
            name = command.name,
            level = command.level,
            abilityScores = command.abilityScores,
            skillProficiencies = command.skillProficiencies,
        ),
    )

    override fun get(id: CharacterId): Character =
        characterRepository.findById(id) ?: throw CharacterNotFoundException(id)

    override fun list(): List<Character> = characterRepository.findAll().sortedBy { it.name.lowercase() }

    override fun updateSkills(id: CharacterId, skillProficiencies: SkillProficiencies): Character =
        characterRepository.save(get(id).copy(skillProficiencies = skillProficiencies))
}
