package com.valsagnapps.dndapp.domain

import com.valsagnapps.dndapp.domain.CharacterClass.BARBARIAN
import com.valsagnapps.dndapp.domain.CharacterClass.BARD
import com.valsagnapps.dndapp.domain.CharacterClass.CLERIC
import com.valsagnapps.dndapp.domain.CharacterClass.DRUID
import com.valsagnapps.dndapp.domain.CharacterClass.FIGHTER
import com.valsagnapps.dndapp.domain.CharacterClass.MONK
import com.valsagnapps.dndapp.domain.CharacterClass.PALADIN
import com.valsagnapps.dndapp.domain.CharacterClass.RANGER
import com.valsagnapps.dndapp.domain.CharacterClass.ROGUE
import com.valsagnapps.dndapp.domain.CharacterClass.SORCERER
import com.valsagnapps.dndapp.domain.CharacterClass.WARLOCK
import com.valsagnapps.dndapp.domain.CharacterClass.WIZARD
import com.valsagnapps.dndapp.domain.features.SUBCLASS_FEATURES

// Subclases del PHB 2014, con el nombre del libro. Se eligen al llegar a CharacterClass.subclassLevel.
enum class Subclass(val characterClass: CharacterClass, val displayName: String, val source: Source = Source.PHB) {
    BERSERKER(BARBARIAN, "Path of the Berserker"),
    TOTEM_WARRIOR(BARBARIAN, "Path of the Totem Warrior"),
    LORE(BARD, "College of Lore"),
    VALOR(BARD, "College of Valor"),
    KNOWLEDGE(CLERIC, "Knowledge Domain"),
    LIFE(CLERIC, "Life Domain"),
    LIGHT(CLERIC, "Light Domain"),
    NATURE(CLERIC, "Nature Domain"),
    TEMPEST(CLERIC, "Tempest Domain"),
    TRICKERY(CLERIC, "Trickery Domain"),
    WAR(CLERIC, "War Domain"),
    LAND(DRUID, "Circle of the Land"),
    MOON(DRUID, "Circle of the Moon"),
    CHAMPION(FIGHTER, "Champion"),
    BATTLE_MASTER(FIGHTER, "Battle Master"),
    ELDRITCH_KNIGHT(FIGHTER, "Eldritch Knight"),
    OPEN_HAND(MONK, "Way of the Open Hand"),
    SHADOW(MONK, "Way of Shadow"),
    FOUR_ELEMENTS(MONK, "Way of the Four Elements"),
    DEVOTION(PALADIN, "Oath of Devotion"),
    ANCIENTS(PALADIN, "Oath of the Ancients"),
    VENGEANCE(PALADIN, "Oath of Vengeance"),
    HUNTER(RANGER, "Hunter"),
    BEAST_MASTER(RANGER, "Beast Master"),
    THIEF(ROGUE, "Thief"),
    ASSASSIN(ROGUE, "Assassin"),
    ARCANE_TRICKSTER(ROGUE, "Arcane Trickster"),
    DRACONIC_BLOODLINE(SORCERER, "Draconic Bloodline"),
    WILD_MAGIC(SORCERER, "Wild Magic"),
    ARCHFEY(WARLOCK, "The Archfey"),
    FIEND(WARLOCK, "The Fiend"),
    GREAT_OLD_ONE(WARLOCK, "The Great Old One"),
    ABJURATION(WIZARD, "School of Abjuration"),
    CONJURATION(WIZARD, "School of Conjuration"),
    DIVINATION(WIZARD, "School of Divination"),
    ENCHANTMENT(WIZARD, "School of Enchantment"),
    EVOCATION(WIZARD, "School of Evocation"),
    ILLUSION(WIZARD, "School of Illusion"),
    NECROMANCY(WIZARD, "School of Necromancy"),
    TRANSMUTATION(WIZARD, "School of Transmutation"),
    ;

    // Features de la subclase en orden de nivel (casi ninguna es SRD: van con resumen propio).
    val features: List<ClassFeature>
        get() = SUBCLASS_FEATURES[this].orEmpty()
}
