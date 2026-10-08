package com.valsagnapps.dndapp.domain

// classes: una entrada por clase (multiclase). La primera es la clase inicial, la que da las salvaciones.
data class Character(
    val id: CharacterId,
    val name: String,
    val classes: List<ClassLevel>,
    val abilityScores: AbilityScores,
    // Se carga a mano: puede ser el promedio o lo que salió en los dados.
    val maxHitPoints: Int,
    val skillProficiencies: SkillProficiencies = SkillProficiencies.NONE,
) {
    init {
        require(name.isNotBlank()) { "name must not be blank" }
        require(name.length <= MAX_NAME_LENGTH) { "name must have at most $MAX_NAME_LENGTH characters" }
        require(classes.isNotEmpty()) { "a character must have at least one class" }
        require(classes.distinctBy { it.characterClass }.size == classes.size) { "classes must not be repeated" }
        require(level in MIN_LEVEL..MAX_LEVEL) { "level must be between $MIN_LEVEL and $MAX_LEVEL, was $level" }
        require(maxHitPoints in MIN_HIT_POINTS..MAX_HIT_POINTS) {
            "maxHitPoints must be between $MIN_HIT_POINTS and $MAX_HIT_POINTS, was $maxHitPoints"
        }
    }

    val level: Int
        get() = classes.sumOf { it.level }

    // +2 en niveles 1-4, +3 en 5-8, ... +6 en 17-20.
    val proficiencyBonus: Int
        get() = 2 + (level - 1) / 4

    // Dados de golpe totales agrupados por tamaño, de mayor a menor (ej. {10: 3, 8: 2}).
    val hitDice: Map<Int, Int>
        get() = classes
            .groupBy { it.characterClass.hitDie }
            .mapValues { (_, levels) -> levels.sumOf { it.level } }
            .toSortedMap(reverseOrder())

    // La clase inicial da sus competencias completas; las que se suman por multiclase, las reducidas.
    val proficiencies: Proficiencies
        get() = classProficiencies.map { (isStarting, granted) ->
            if (isStarting) granted.starting else granted.multiclass
        }
            .reduce(Proficiencies::plus)

    // Skills a elegir por cada clase, en el mismo orden que `classes`. Solo sugerencia: no se valida.
    val skillChoices: List<SkillChoice>
        get() = classProficiencies.map { (isStarting, granted) ->
            if (isStarting) granted.startingSkills else granted.multiclassSkills
        }

    // Competencias de cada clase, marcando cuál es la inicial.
    private val classProficiencies: List<Pair<Boolean, ClassProficiencies>>
        get() = classes.mapIndexed { index, classLevel -> (index == 0) to classLevel.characterClass.proficiencies }

    // Reemplaza las clases conservando la subclase de las que siguen con nivel para tenerla:
    // si una clase cambia o baja del nivel de subclase, la subclase se borra.
    fun withClasses(newClasses: List<ClassLevel>): Character = copy(
        classes = newClasses.map { new ->
            val kept = classes.find { it.characterClass == new.characterClass }?.subclass
            new.copy(subclass = new.subclass ?: kept?.takeIf { new.canHaveSubclass })
        },
    )

    // Elige (o quita, con null) la subclase de una de sus clases.
    fun withSubclass(characterClass: CharacterClass, subclass: Subclass?): Character {
        require(classes.any { it.characterClass == characterClass }) { "the character has no $characterClass levels" }
        return copy(
            classes = classes.map { if (it.characterClass == characterClass) it.copy(subclass = subclass) else it },
        )
    }

    fun savingThrowProficiency(ability: Ability): Proficiency =
        if (ability in classes.first().characterClass.savingThrows) Proficiency.PROFICIENT else Proficiency.NONE

    fun savingThrowBonus(ability: Ability): Int =
        abilityScores.modifierOf(ability) + proficiencyBonus * savingThrowProficiency(ability).multiplier

    fun skillBonus(skill: Skill): Int =
        abilityScores.modifierOf(skill.ability) + proficiencyBonus * skillProficiencies.of(skill).multiplier

    val passivePerception: Int
        get() = PASSIVE_BASE + skillBonus(Skill.PERCEPTION)

    companion object {
        const val MIN_LEVEL = 1
        const val MAX_LEVEL = 20
        const val MAX_NAME_LENGTH = 100
        const val MIN_HIT_POINTS = 1
        const val MAX_HIT_POINTS = 999
        private const val PASSIVE_BASE = 10
    }
}
