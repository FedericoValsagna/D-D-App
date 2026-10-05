package com.valsagnapps.dndapp.adapter.outbound.persistence

import com.valsagnapps.dndapp.domain.CharacterClass
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
class ClassLevelEmbeddable(
    @Column(name = "character_class")
    @Enumerated(EnumType.STRING)
    val characterClass: CharacterClass,
    val level: Int,
)
