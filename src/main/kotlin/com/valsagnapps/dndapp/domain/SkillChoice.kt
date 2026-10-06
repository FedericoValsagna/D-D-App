package com.valsagnapps.dndapp.domain

// Cuántas skills puede elegir el personaje por una clase, y de cuáles. Es una sugerencia: las skills
// se marcan libres en la hoja porque trasfondo, raza y dotes también dan competencias.
data class SkillChoice(val count: Int, val options: Set<Skill>) {
    init {
        require(count >= 0) { "count must not be negative" }
        require(count <= options.size) { "cannot choose $count skills from ${options.size} options" }
    }

    companion object {
        val NONE = SkillChoice(0, emptySet())

        fun any(count: Int) = SkillChoice(count, Skill.entries.toSet())
    }
}
