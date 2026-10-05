package com.valsagnapps.dndapp.application.port.inbound

import com.valsagnapps.dndapp.domain.Character
import com.valsagnapps.dndapp.domain.CharacterId
import com.valsagnapps.dndapp.domain.ClassLevel

interface UpdateClassesUseCase {
    // Reemplaza todas las clases del personaje; la primera es la clase inicial.
    fun updateClasses(id: CharacterId, classes: List<ClassLevel>): Character
}
