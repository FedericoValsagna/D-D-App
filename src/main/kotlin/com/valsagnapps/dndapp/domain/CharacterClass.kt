package com.valsagnapps.dndapp.domain

import com.valsagnapps.dndapp.domain.Ability.CHARISMA
import com.valsagnapps.dndapp.domain.Ability.CONSTITUTION
import com.valsagnapps.dndapp.domain.Ability.DEXTERITY
import com.valsagnapps.dndapp.domain.Ability.INTELLIGENCE
import com.valsagnapps.dndapp.domain.Ability.STRENGTH
import com.valsagnapps.dndapp.domain.Ability.WISDOM
import com.valsagnapps.dndapp.domain.features.CLASS_FEATURES

// Clases del PHB 2014: dado de golpe, tiradas de salvación con competencia, nivel en que se elige la subclase
// y demás competencias (ver ClassProficiencies).
@Suppress("MagicNumber")
enum class CharacterClass(
    val hitDie: Int,
    val savingThrows: Set<Ability>,
    val subclassLevel: Int,
    val source: Source = Source.PHB,
) {
    BARBARIAN(12, setOf(STRENGTH, CONSTITUTION), 3),
    BARD(8, setOf(DEXTERITY, CHARISMA), 3),
    CLERIC(8, setOf(WISDOM, CHARISMA), 1),
    DRUID(8, setOf(INTELLIGENCE, WISDOM), 2),
    FIGHTER(10, setOf(STRENGTH, CONSTITUTION), 3),
    MONK(8, setOf(STRENGTH, DEXTERITY), 3),
    PALADIN(10, setOf(WISDOM, CHARISMA), 3),
    RANGER(10, setOf(STRENGTH, DEXTERITY), 3),
    ROGUE(8, setOf(DEXTERITY, INTELLIGENCE), 3),
    SORCERER(6, setOf(CONSTITUTION, CHARISMA), 1),
    WARLOCK(8, setOf(WISDOM, CHARISMA), 1),
    WIZARD(6, setOf(INTELLIGENCE, WISDOM), 2),
    ;

    val proficiencies: ClassProficiencies
        get() = CLASS_PROFICIENCIES.getValue(this)

    val subclasses: List<Subclass>
        get() = Subclass.entries.filter { it.characterClass == this }

    // Features de la clase en orden de nivel (sin las de subclase).
    val features: List<ClassFeature>
        get() = CLASS_FEATURES[this].orEmpty()
}
