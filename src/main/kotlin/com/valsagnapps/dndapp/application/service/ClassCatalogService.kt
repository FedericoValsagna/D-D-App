package com.valsagnapps.dndapp.application.service

import com.valsagnapps.dndapp.application.port.inbound.ListClassesUseCase
import com.valsagnapps.dndapp.domain.CharacterClass

// El catálogo vive en el dominio como código: no pasa por la base.
class ClassCatalogService : ListClassesUseCase {
    override fun listClasses(): List<CharacterClass> = CharacterClass.entries
}
