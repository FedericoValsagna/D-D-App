package com.valsagnapps.dndapp.application.port.inbound

import com.valsagnapps.dndapp.domain.CharacterClass

interface ListClassesUseCase {
    // Catálogo de clases (con sus subclases), en el orden del enum.
    fun listClasses(): List<CharacterClass>
}
