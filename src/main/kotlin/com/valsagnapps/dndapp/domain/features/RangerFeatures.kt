package com.valsagnapps.dndapp.domain.features

import com.valsagnapps.dndapp.domain.ClassFeature

// Ranger y Hunter: textos del SRD 5.1 y resúmenes propios.
internal val RANGER_FEATURES = listOf(
    ClassFeature(
        id = "RANGER_FAVORED_ENEMY",
        name = "Favored Enemy",
        level = 1,
        summary =
        "Choose a creature type (or two humanoid races) as favored enemy: advantage on Wisdom (Survival) checks " +
            "to track them and on Intelligence checks to recall information about them, and you learn one of their " +
            "languages. One more favored enemy at 6th and 14th level.",
        srdText =
        "Beginning at 1st level, you have significant experience studying, tracking, hunting, and even talking " +
            "to a certain type of enemy.\n\nChoose a type of favored enemy: aberrations, beasts, celestials, " +
            "constructs, dragons, elementals, fey, fiends, giants, monstrosities, oozes, plants, or undead. " +
            "Alternatively, you can select two races of humanoid (such as gnolls and orcs) as favored " +
            "enemies.\n\nYou have advantage on Wisdom (Survival) checks to track your favored enemies, as well as " +
            "on Intelligence checks to recall information about them.\n\nWhen you gain this feature, you also learn " +
            "one language of your choice that is spoken by your favored enemies, if they speak one at all.\n\nYou " +
            "choose one additional favored enemy, as well as an associated language, at 6th and 14th level. As you " +
            "gain levels, your choices should reflect the types of monsters you have encountered on your " +
            "adventures.",
    ),
    ClassFeature(
        id = "RANGER_NATURAL_EXPLORER",
        name = "Natural Explorer",
        level = 1,
        summary =
        "Choose a favored terrain: double proficiency bonus on Intelligence and Wisdom checks about it with " +
            "skills you are proficient in. Traveling there for an hour or more, your group isn't slowed by " +
            "difficult terrain and can't get lost except by magic; you stay alert while doing other things, move " +
            "stealthily alone at normal pace, find twice the food and learn the number, size and timing of the " +
            "creatures you track. One more terrain at 6th and 10th level.",
        srdText =
        "You are particularly familiar with one type of natural environment and are adept at traveling and " +
            "surviving in such regions. Choose one type of favored terrain: arctic, coast, desert, forest, " +
            "grassland, mountain, or swamp. When you make an Intelligence or Wisdom check related to your favored " +
            "terrain, your proficiency bonus is doubled if you are using a skill that you're proficient " +
            "in.\n\nWhile traveling for an hour or more in your favored terrain, you gain the following " +
            "benefits:\n\n- Difficult terrain doesn't slow your group's travel.\n- Your group can't become lost " +
            "except by magical means.\n- Even when you are engaged in another activity while traveling (such as " +
            "foraging, navigating, or tracking), you remain alert to danger.\n- If you are traveling alone, you can " +
            "move stealthily at a normal pace.\n- When you forage, you find twice as much food as you normally " +
            "would.\n- While tracking other creatures, you also learn their exact number, their sizes, and how long " +
            "ago they passed through the area.\n\nYou choose additional favored terrain types at 6th and 10th " +
            "level.",
    ),
    ClassFeature(
        id = "RANGER_FIGHTING_STYLE",
        name = "Fighting Style",
        level = 2,
        summary =
        "Choose one: Archery (+2 to ranged weapon attack rolls), Defense (+1 AC while wearing armor), Dueling " +
            "(+2 damage with a one-handed melee weapon and no other weapon) or Two-Weapon Fighting (add your " +
            "ability modifier to the damage of the second attack).",
        srdText =
        "Archery. You gain a +2 bonus to attack rolls you make with ranged weapons.\n\nDefense. While you are " +
            "wearing armor, you gain a +1 bonus to AC.\n\nDueling. When you are wielding a melee weapon in one hand " +
            "and no other weapons, you gain a +2 bonus to damage rolls with that weapon.\n\nTwo-Weapon Fighting. " +
            "When you engage in two-weapon fighting, you can add your ability modifier to the damage of the second " +
            "attack.",
    ),
    ClassFeature(
        id = "RANGER_SPELLCASTING",
        name = "Spellcasting",
        level = 2,
        summary =
        "Known-spells caster with Wisdom: you learn ranger spells as you level up and can swap one for another " +
            "each level. Slots recover on a long rest.",
        srdText =
        "By the time you reach 2nd level, you have learned to use the magical essence of nature to cast spells, " +
            "much as a druid does.\n\nSpell Slots. The Ranger table shows how many spell slots you have to cast " +
            "your spells of 1st level and higher. To cast one of these spells, you must expend a slot of the " +
            "spell's level or higher. You regain all expended spell slots when you finish a long rest.\n\nFor " +
            "example, if you know the 1st-level spell animal friendship and have a 1st-level and a 2nd-level spell " +
            "slot available, you can cast animal friendship using either slot.\n\nSpells Known of 1st Level and " +
            "Higher. You know two 1st-level spells of your choice from the ranger spell list.\n\nThe Spells Known " +
            "column of the Ranger table shows when you learn more ranger spells of your choice. Each of these " +
            "spells must be of a level for which you have spell slots. For instance, when you reach 5th level in " +
            "this class, you can learn one new spell of 1st or 2nd level.\n\nAdditionally, when you gain a level in " +
            "this class, you can choose one of the ranger spells you know and replace it with another spell from " +
            "the ranger spell list, which also must be of a level for which you have spell slots.\n\nSpellcasting " +
            "Ability. Wisdom is your spellcasting ability for your ranger spells, since your magic draws on your " +
            "attunement to nature. You use your Wisdom whenever a spell refers to your spellcasting ability. In " +
            "addition, you use your Wisdom modifier when setting the saving throw DC for a ranger spell you cast " +
            "and when making an attack roll with one.\n\nSpell save DC = 8 + your proficiency bonus + your Wisdom " +
            "modifier.\n\nSpell attack modifier = your proficiency bonus + your Wisdom modifier.",
    ),
    ClassFeature(
        id = "RANGER_PRIMEVAL_AWARENESS",
        name = "Primeval Awareness",
        level = 3,
        summary =
        "Action and a ranger spell slot: for 1 minute per slot level, sense whether there are aberrations, " +
            "celestials, dragons, elementals, fey, fiends or undead within 1 mile (6 in your favored terrain), but " +
            "not where or how many.",
        srdText =
        "Beginning at 3rd level, you can use your action and expend one ranger spell slot to focus your " +
            "awareness on the region around you. For 1 minute per level of the spell slot you expend, you can sense " +
            "whether the following types of creatures are present within 1 mile of you (or within up to 6 miles if " +
            "you are in your favored terrain): aberrations, celestials, dragons, elementals, fey, fiends, and " +
            "undead. This feature doesn't reveal the creatures' location or number.",
    ),
    ClassFeature(
        id = "RANGER_ABILITY_SCORE_IMPROVEMENT",
        name = "Ability Score Improvement",
        level = 4,
        summary = "At 4th, 8th, 12th, 16th and 19th level: +2 to one ability score or +1 to two (maximum 20).",
        srdText =
        "When you reach 4th level, and again at 8th, 12th, 16th, and 19th level, you can increase one ability " +
            "score of your choice by 2, or you can increase two ability scores of your choice by 1. As normal, you " +
            "can't increase an ability score above 20 using this feature.",
    ),
    ClassFeature(
        id = "RANGER_EXTRA_ATTACK",
        name = "Extra Attack",
        level = 5,
        summary = "Attack twice, instead of once, when you take the Attack action.",
        srdText =
        "Beginning at 5th level, you can attack twice, instead of once, whenever you take the Attack action on " +
            "your turn.",
    ),
    ClassFeature(
        id = "RANGER_LANDS_STRIDE",
        name = "Land's Stride",
        level = 8,
        summary =
        "Nonmagical difficult terrain costs no extra movement, and nonmagical plants don't slow or hurt you. " +
            "Advantage on saves against plants magically created or manipulated to impede movement, like entangle.",
        srdText =
        "Starting at 8th level, moving through nonmagical difficult terrain costs you no extra movement. You " +
            "can also pass through nonmagical plants without being slowed by them and without taking damage from " +
            "them if they have thorns, spines, or a similar hazard.\n\nIn addition, you have advantage on saving " +
            "throws against plants that are magically created or manipulated to impede movement, such those created " +
            "by the entangle spell.",
    ),
    ClassFeature(
        id = "RANGER_HIDE_IN_PLAIN_SIGHT",
        name = "Hide in Plain Sight",
        level = 10,
        summary =
        "Spend 1 minute camouflaging yourself with natural materials. Pressed against a surface at least your " +
            "size, you get +10 to Dexterity (Stealth) checks until you move or take an action or reaction.",
        srdText =
        "Starting at 10th level, you can spend 1 minute creating camouflage for yourself. You must have access " +
            "to fresh mud, dirt, plants, soot, and other naturally occurring materials with which to create your " +
            "camouflage.\n\nOnce you are camouflaged in this way, you can try to hide by pressing yourself up " +
            "against a solid surface, such as a tree or wall, that is at least as tall and wide as you are. You " +
            "gain a +10 bonus to Dexterity (Stealth) checks as long as you remain there without moving or taking " +
            "actions. Once you move or take an action or a reaction, you must camouflage yourself again to gain " +
            "this benefit.",
    ),
    ClassFeature(
        id = "RANGER_VANISH",
        name = "Vanish",
        level = 14,
        summary =
        "Hide as a bonus action, and you can't be tracked by nonmagical means unless you choose to leave a " +
            "trail.",
        srdText =
        "Starting at 14th level, you can use the Hide action as a bonus action on your turn. Also, you can't be " +
            "tracked by nonmagical means, unless you choose to leave a trail.",
    ),
    ClassFeature(
        id = "RANGER_FERAL_SENSES",
        name = "Feral Senses",
        level = 18,
        summary =
        "Not seeing a creature doesn't give you disadvantage when attacking it, and you know where invisible " +
            "creatures within 30 ft are, unless they are hidden from you or you are blinded or deafened.",
        srdText =
        "At 18th level, you gain preternatural senses that help you fight creatures you can't see. When you " +
            "attack a creature you can't see, your inability to see it doesn't impose disadvantage on your attack " +
            "rolls against it.\n\nYou are also aware of the location of any invisible creature within 30 feet of " +
            "you, provided that the creature isn't hidden from you and you aren't blinded or deafened.",
    ),
    ClassFeature(
        id = "RANGER_FOE_SLAYER",
        name = "Foe Slayer",
        level = 20,
        summary =
        "Once on each of your turns, add your Wisdom modifier to the attack or damage roll of an attack against " +
            "a favored enemy, before or after rolling.",
        srdText =
        "At 20th level, you become an unparalleled hunter of your enemies. Once on each of your turns, you can " +
            "add your Wisdom modifier to the attack roll or the damage roll of an attack you make against one of " +
            "your favored enemies. You can choose to use this feature before or after the roll, but before any " +
            "effects of the roll are applied.",
    ),
)

internal val HUNTER_FEATURES = listOf(
    ClassFeature(
        id = "HUNTER_HUNTERS_PREY",
        name = "Hunter's Prey",
        level = 3,
        summary =
        "Choose one: Colossus Slayer (once per turn, +1d8 damage to a creature below its hit point maximum), " +
            "Giant Killer (reaction: attack a Large or larger creature within 5 ft that attacks you) or Horde " +
            "Breaker (once per turn, an extra attack against another creature within 5 ft of your target).",
        srdText =
        "At 3rd level, you gain one of the following features of your choice.\n\nColossus Slayer. Your tenacity " +
            "can wear down the most potent foes. When you hit a creature with a weapon attack, the creature takes " +
            "an extra 1d8 damage if it's below its hit point maximum. You can deal this extra damage only once per " +
            "turn.\n\nGiant Killer. When a Large or larger creature within 5 feet of you hits or misses you with an " +
            "attack, you can use your reaction to attack that creature immediately after its attack, provided that " +
            "you can see the creature.\n\nHorde Breaker. Once on each of your turns when you make a weapon attack, " +
            "you can make another attack with the same weapon against a different creature that is within 5 feet of " +
            "the original target and within range of your weapon.",
    ),
    ClassFeature(
        id = "HUNTER_DEFENSIVE_TACTICS",
        name = "Defensive Tactics",
        level = 7,
        summary =
        "Choose one: Escape the Horde (opportunity attacks against you have disadvantage), Multiattack Defense " +
            "(after a creature hits you, +4 AC against its other attacks that turn) or Steel Will (advantage on " +
            "saves against being frightened).",
        srdText =
        "At 7th level, you gain one of the following features of your choice.\n\nEscape the Horde. Opportunity " +
            "attacks against you are made with disadvantage.\n\nMultiattack Defense. When a creature hits you with " +
            "an attack, you gain a +4 bonus to AC against all subsequent attacks made by that creature for the rest " +
            "of the turn.\n\nSteel Will. You have advantage on saving throws against being frightened.",
    ),
    ClassFeature(
        id = "HUNTER_MULTIATTACK",
        name = "Multiattack",
        level = 11,
        summary =
        "Choose one: Volley (action: a ranged attack against every creature within 10 ft of a point in range) " +
            "or Whirlwind Attack (action: a melee attack against every creature within 5 ft).",
        srdText =
        "At 11th level, you gain one of the following features of your choice.\n\nVolley. You can use your " +
            "action to make a ranged attack against any number of creatures within 10 feet of a point you can see " +
            "within your weapon's range. You must have ammunition for each target, as normal, and you make a " +
            "separate attack roll for each target.\n\nWhirlwind Attack. You can use your action to make a melee " +
            "attack against any number of creatures within 5 feet of you, with a separate attack roll for each " +
            "target.",
    ),
    ClassFeature(
        id = "HUNTER_SUPERIOR_HUNTERS_DEFENSE",
        name = "Superior Hunter's Defense",
        level = 15,
        summary =
        "Choose one: Evasion (no damage on a successful Dexterity save for half, half on a failure), Stand " +
            "Against the Tide (reaction: a creature that misses you in melee repeats the attack against another " +
            "creature) or Uncanny Dodge (reaction: halve the damage of an attack from an attacker you can see).",
        srdText =
        "At 15th level, you gain one of the following features of your choice.\n\nEvasion. When you are " +
            "subjected to an effect, such as a red dragon's fiery breath or a lightning bolt spell, that allows you " +
            "to make a Dexterity saving throw to take only half damage, you instead take no damage if you succeed " +
            "on the saving throw, and only half damage if you fail.\n\nStand Against the Tide. When a hostile " +
            "creature misses you with a melee attack, you can use your reaction to force that creature to repeat " +
            "the same attack against another creature (other than itself) of your choice.\n\nUncanny Dodge. When an " +
            "attacker that you can see hits you with an attack, you can use your reaction to halve the attack's " +
            "damage against you.",
    ),
)
