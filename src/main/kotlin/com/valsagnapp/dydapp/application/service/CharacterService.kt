package com.valsagnapp.dydapp.application.service

import com.valsagnapp.dydapp.application.port.inbound.CreateCharacterCommand
import com.valsagnapp.dydapp.application.port.inbound.CreateCharacterUseCase
import com.valsagnapp.dydapp.application.port.inbound.GetCharacterUseCase
import com.valsagnapp.dydapp.application.port.outbound.CharacterRepository
import com.valsagnapp.dydapp.domain.Character
import com.valsagnapp.dydapp.domain.CharacterId
import com.valsagnapp.dydapp.domain.CharacterNotFoundException

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
