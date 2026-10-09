package com.valsagnapps.dndapp.domain.features

import com.valsagnapps.dndapp.domain.CharacterClass
import com.valsagnapps.dndapp.domain.ClassFeature
import com.valsagnapps.dndapp.domain.Subclass

// Features por clase y subclase, en orden de nivel. Las clases y subclases sin cargar todavía no tienen features.
internal val CLASS_FEATURES: Map<CharacterClass, List<ClassFeature>> = mapOf(
    CharacterClass.CLERIC to CLERIC_FEATURES,
    CharacterClass.RANGER to RANGER_FEATURES,
)

internal val SUBCLASS_FEATURES: Map<Subclass, List<ClassFeature>> = mapOf(
    Subclass.LIFE to LIFE_FEATURES,
    Subclass.HUNTER to HUNTER_FEATURES,
)
