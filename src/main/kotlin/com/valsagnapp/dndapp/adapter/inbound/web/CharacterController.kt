package com.valsagnapp.dndapp.adapter.inbound.web

import com.valsagnapp.dndapp.application.port.inbound.CreateCharacterUseCase
import com.valsagnapp.dndapp.application.port.inbound.GetCharacterUseCase
import com.valsagnapp.dndapp.domain.CharacterId
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/characters")
class CharacterController(
    private val createCharacter: CreateCharacterUseCase,
    private val getCharacter: GetCharacterUseCase,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CreateCharacterRequest): CharacterResponse =
        CharacterResponse.from(createCharacter.create(request.toCommand()))

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): CharacterResponse = CharacterResponse.from(getCharacter.get(CharacterId(id)))
}
