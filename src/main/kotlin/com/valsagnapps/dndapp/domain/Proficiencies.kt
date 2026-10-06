package com.valsagnapps.dndapp.domain

// Competencias de armaduras, armas y herramientas. Se suman entre clases (multiclase) con `plus`.
data class Proficiencies(
    val armor: Set<ArmorProficiency> = emptySet(),
    val weapons: Set<WeaponProficiency> = emptySet(),
    val tools: Set<ToolProficiency> = emptySet(),
    val toolChoices: List<ToolChoice> = emptyList(),
) {
    // Las armas puntuales que ya cubre una categoría completa se descartan (SIMPLE ya incluye DAGGER).
    operator fun plus(other: Proficiencies): Proficiencies {
        val weapons = weapons + other.weapons
        val wholeCategories = weapons.filter { it.isWholeCategory }.map { it.category }.toSet()
        return Proficiencies(
            armor = armor + other.armor,
            weapons = weapons.filterTo(mutableSetOf()) { it.isWholeCategory || it.category !in wholeCategories },
            tools = tools + other.tools,
            toolChoices = toolChoices + other.toolChoices,
        )
    }

    companion object {
        val NONE = Proficiencies()
    }
}
