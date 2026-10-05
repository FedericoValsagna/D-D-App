package com.valsagnapps.dndapp.application.port.inbound

import com.valsagnapps.dndapp.domain.Character
import com.valsagnapps.dndapp.domain.CharacterId
import com.valsagnapps.dndapp.domain.SkillProficiencies

interface UpdateSkillsUseCase {
    // Reemplaza todas las competencias en skills del personaje.
    fun updateSkills(id: CharacterId, skillProficiencies: SkillProficiencies): Character
}
