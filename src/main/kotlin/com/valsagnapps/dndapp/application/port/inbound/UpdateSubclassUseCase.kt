package com.valsagnapps.dndapp.application.port.inbound

import com.valsagnapps.dndapp.domain.Character
import com.valsagnapps.dndapp.domain.CharacterClass
import com.valsagnapps.dndapp.domain.CharacterId
import com.valsagnapps.dndapp.domain.Subclass

interface UpdateSubclassUseCase {
    // Elige la subclase de una de las clases del personaje; null la quita.
    fun updateSubclass(id: CharacterId, characterClass: CharacterClass, subclass: Subclass?): Character
}
