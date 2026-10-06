package com.valsagnapps.dndapp.domain

import com.valsagnapps.dndapp.domain.Ability.CHARISMA
import com.valsagnapps.dndapp.domain.Ability.CONSTITUTION
import com.valsagnapps.dndapp.domain.Ability.DEXTERITY
import com.valsagnapps.dndapp.domain.Ability.INTELLIGENCE
import com.valsagnapps.dndapp.domain.Ability.STRENGTH
import com.valsagnapps.dndapp.domain.Ability.WISDOM

// Clases del PHB 2014: dado de golpe, tiradas de salvación con competencia y demás competencias
// (ver ClassProficiencies).
@Suppress("MagicNumber")
enum class CharacterClass(val hitDie: Int, val savingThrows: Set<Ability>, val source: Source = Source.PHB) {
    BARBARIAN(12, setOf(STRENGTH, CONSTITUTION)),
    BARD(8, setOf(DEXTERITY, CHARISMA)),
    CLERIC(8, setOf(WISDOM, CHARISMA)),
    DRUID(8, setOf(INTELLIGENCE, WISDOM)),
    FIGHTER(10, setOf(STRENGTH, CONSTITUTION)),
    MONK(8, setOf(STRENGTH, DEXTERITY)),
    PALADIN(10, setOf(WISDOM, CHARISMA)),
    RANGER(10, setOf(STRENGTH, DEXTERITY)),
    ROGUE(8, setOf(DEXTERITY, INTELLIGENCE)),
    SORCERER(6, setOf(CONSTITUTION, CHARISMA)),
    WARLOCK(8, setOf(WISDOM, CHARISMA)),
    WIZARD(6, setOf(INTELLIGENCE, WISDOM)),
    ;

    val proficiencies: ClassProficiencies
        get() = CLASS_PROFICIENCIES.getValue(this)
}
