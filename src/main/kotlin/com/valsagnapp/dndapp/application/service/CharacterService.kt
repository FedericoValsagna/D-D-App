package com.valsagnapp.dndapp.application.service

import com.valsagnapp.dndapp.application.port.inbound.CreateCharacterCommand
import com.valsagnapp.dndapp.application.port.inbound.CreateCharacterUseCase
import com.valsagnapp.dndapp.application.port.inbound.GetCharacterUseCase
import com.valsagnapp.dndapp.application.port.outbound.CharacterRepository
import com.valsagnapp.dndapp.domain.Character
import com.valsagnapp.dndapp.domain.CharacterId
import com.valsagnapp.dndapp.domain.CharacterNotFoundException

class CharacterService(private val characterRepository: CharacterRepository) :
    CreateCharacterUseCase,
    GetCharacterUseCase {
    override fun create(command: CreateCharacterCommand): Character = characterRepository.save(
        Character(
            id = CharacterId.new(),
            name = command.name,
            level = command.level,
            abilityScores = command.abilityScores,
        ),
    )

    override fun get(id: CharacterId): Character =
        characterRepository.findById(id) ?: throw CharacterNotFoundException(id)
}
