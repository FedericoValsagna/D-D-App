package com.valsagnapps.dndapp.adapter.inbound.web

import com.fasterxml.jackson.annotation.JsonProperty
import com.valsagnapps.dndapp.domain.CharacterClass
import com.valsagnapps.dndapp.domain.Source

// Entrada del catálogo de clases: lo que la app necesita para editar la clase y la subclase.
data class ClassResponse(
    @get:JsonProperty("class") val characterClass: CharacterClass,
    val hitDie: Int,
    val subclassLevel: Int,
    val subclasses: List<SubclassResponse>,
    val source: Source,
) {
    companion object {
        fun from(characterClass: CharacterClass) = ClassResponse(
            characterClass = characterClass,
            hitDie = characterClass.hitDie,
            subclassLevel = characterClass.subclassLevel,
            subclasses = characterClass.subclasses.map { SubclassResponse.from(it) },
            source = characterClass.source,
        )
    }
}
