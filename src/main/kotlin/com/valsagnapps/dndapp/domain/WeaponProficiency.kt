package com.valsagnapps.dndapp.domain

enum class WeaponCategory {
    SIMPLE,
    MARTIAL,
}

// Una categoría entera (SIMPLE, MARTIAL) o un arma puntual. Las armas puntuales saben a qué categoría
// pertenecen: si el personaje ya tiene la categoría, el arma suelta sobra.
enum class WeaponProficiency(val category: WeaponCategory, val isWholeCategory: Boolean = false) {
    SIMPLE(WeaponCategory.SIMPLE, isWholeCategory = true),
    MARTIAL(WeaponCategory.MARTIAL, isWholeCategory = true),
    CLUB(WeaponCategory.SIMPLE),
    DAGGER(WeaponCategory.SIMPLE),
    DART(WeaponCategory.SIMPLE),
    JAVELIN(WeaponCategory.SIMPLE),
    LIGHT_CROSSBOW(WeaponCategory.SIMPLE),
    MACE(WeaponCategory.SIMPLE),
    QUARTERSTAFF(WeaponCategory.SIMPLE),
    SICKLE(WeaponCategory.SIMPLE),
    SLING(WeaponCategory.SIMPLE),
    SPEAR(WeaponCategory.SIMPLE),
    HAND_CROSSBOW(WeaponCategory.MARTIAL),
    LONGSWORD(WeaponCategory.MARTIAL),
    RAPIER(WeaponCategory.MARTIAL),
    SCIMITAR(WeaponCategory.MARTIAL),
    SHORTSWORD(WeaponCategory.MARTIAL),
}
