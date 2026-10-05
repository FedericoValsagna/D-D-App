package com.valsagnapps.dndapp.domain

// Competencia por skill. Las que no están son NONE: se guardan solo las que suman algo,
// así dos mapas que dicen lo mismo (con o sin NONE explícito) son iguales.
class SkillProficiencies private constructor(private val levels: Map<Skill, Proficiency>) {
    fun of(skill: Skill): Proficiency = levels[skill] ?: Proficiency.NONE

    fun toMap(): Map<Skill, Proficiency> = levels

    override fun equals(other: Any?): Boolean = other is SkillProficiencies && levels == other.levels

    override fun hashCode(): Int = levels.hashCode()

    override fun toString(): String = "SkillProficiencies($levels)"

    companion object {
        val NONE = SkillProficiencies(emptyMap())

        fun of(levels: Map<Skill, Proficiency>) = SkillProficiencies(levels.filterValues { it != Proficiency.NONE })
    }
}
