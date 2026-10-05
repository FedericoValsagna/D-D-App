package com.valsagnapps.dndapp.application.service

import com.valsagnapps.dndapp.application.port.inbound.CreateCharacterCommand
import com.valsagnapps.dndapp.application.port.inbound.CreateCharacterUseCase
import com.valsagnapps.dndapp.application.port.inbound.GetCharacterUseCase
import com.valsagnapps.dndapp.application.port.inbound.ListCharactersUseCase
import com.valsagnapps.dndapp.application.port.inbound.UpdateClassesUseCase
import com.valsagnapps.dndapp.application.port.inbound.UpdateHitPointsUseCase
import com.valsagnapps.dndapp.application.port.inbound.UpdateSkillsUseCase
import com.valsagnapps.dndapp.application.port.outbound.CharacterRepository
import com.valsagnapps.dndapp.domain.Character
import com.valsagnapps.dndapp.domain.CharacterId
import com.valsagnapps.dndapp.domain.CharacterNotFoundException
import com.valsagnapps.dndapp.domain.ClassLevel
import com.valsagnapps.dndapp.domain.SkillProficiencies

class CharacterService(private val characterRepository: CharacterRepository) :
    CreateCharacterUseCase,
    GetCharacterUseCase,
    ListCharactersUseCase,
    UpdateSkillsUseCase,
    UpdateClassesUseCase,
    UpdateHitPointsUseCase {
    override fun create(command: CreateCharacterCommand): Character = characterRepository.save(
        Character(
            id = CharacterId.new(),
            name = command.name,
            classes = command.classes,
            abilityScores = command.abilityScores,
            maxHitPoints = command.maxHitPoints,
            skillProficiencies = command.skillProficiencies,
        ),
    )

    override fun get(id: CharacterId): Character =
        characterRepository.findById(id) ?: throw CharacterNotFoundException(id)

    override fun list(): List<Character> = characterRepository.findAll().sortedBy { it.name.lowercase() }

    override fun updateSkills(id: CharacterId, skillProficiencies: SkillProficiencies): Character =
        characterRepository.save(get(id).copy(skillProficiencies = skillProficiencies))

    override fun updateClasses(id: CharacterId, classes: List<ClassLevel>): Character =
        characterRepository.save(get(id).copy(classes = classes))

    override fun updateMaxHitPoints(id: CharacterId, maxHitPoints: Int): Character =
        characterRepository.save(get(id).copy(maxHitPoints = maxHitPoints))
}
