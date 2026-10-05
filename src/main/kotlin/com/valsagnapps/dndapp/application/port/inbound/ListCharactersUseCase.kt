package com.valsagnapps.dndapp.application.port.inbound

import com.valsagnapps.dndapp.domain.Character

interface ListCharactersUseCase {
    fun list(): List<Character>
}
