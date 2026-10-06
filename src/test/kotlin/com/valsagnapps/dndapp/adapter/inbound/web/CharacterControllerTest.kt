package com.valsagnapps.dndapp.adapter.inbound.web

import com.valsagnapps.dndapp.application.service.CharacterService
import com.valsagnapps.dndapp.application.service.InMemoryCharacterRepository
import com.valsagnapps.dndapp.application.service.createCommand
import com.valsagnapps.dndapp.domain.CharacterClass
import com.valsagnapps.dndapp.domain.Proficiency
import com.valsagnapps.dndapp.domain.Skill
import com.valsagnapps.dndapp.domain.SkillProficiencies
import com.valsagnapps.dndapp.domain.abilityScores
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.hasItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.util.UUID
import kotlin.test.Test

@WebMvcTest(CharacterController::class)
@Import(CharacterControllerTest.Config::class)
class CharacterControllerTest {
    @TestConfiguration
    class Config {
        @Bean
        fun characterService() = CharacterService(InMemoryCharacterRepository())
    }

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var characterService: CharacterService

    @Test
    fun `creates a character and returns derived stats`() {
        mockMvc.post("/api/v1/characters") {
            contentType = MediaType.APPLICATION_JSON
            content = validRequest(name = "Regdar", level = 5, strength = 17)
        }.andExpect {
            status { isCreated() }
            jsonPath("$.id") { exists() }
            jsonPath("$.name") { value("Regdar") }
            jsonPath("$.level") { value(5) }
            jsonPath("$.classes[0].class") { value("FIGHTER") }
            jsonPath("$.classes[0].level") { value(5) }
            jsonPath("$.classes[0].hitDie") { value(10) }
            jsonPath("$.maxHitPoints") { value(44) }
            jsonPath("$.hitDice[0].die") { value(10) }
            jsonPath("$.hitDice[0].count") { value(5) }
            jsonPath("$.proficiencyBonus") { value(3) }
            jsonPath("$.abilities.STRENGTH.score") { value(17) }
            jsonPath("$.savingThrows.STRENGTH.proficiency") { value("PROFICIENT") }
            jsonPath("$.savingThrows.STRENGTH.bonus") { value(6) }
            jsonPath("$.savingThrows.DEXTERITY.proficiency") { value("NONE") }
            jsonPath("$.savingThrows.DEXTERITY.bonus") { value(0) }
            jsonPath("$.savingThrows.length()") { value(6) }
            jsonPath("$.abilities.STRENGTH.modifier") { value(3) }
            jsonPath("$.skills.ATHLETICS.ability") { value("STRENGTH") }
            jsonPath("$.skills.ATHLETICS.proficiency") { value("NONE") }
            jsonPath("$.skills.ATHLETICS.bonus") { value(3) }
            jsonPath("$.skills.length()") { value(18) }
            jsonPath("$.passivePerception") { value(10) }
            jsonPath("$.classes[0].skillChoices.count") { value(2) }
            jsonPath("$.classes[0].skillChoices.options.length()") { value(8) }
            jsonPath("$.proficiencies.armor") { value(contains("LIGHT", "MEDIUM", "HEAVY", "SHIELDS")) }
            jsonPath("$.proficiencies.weapons") { value(contains("SIMPLE", "MARTIAL")) }
            jsonPath("$.proficiencies.tools") { isEmpty() }
            jsonPath("$.proficiencies.toolChoices") { isEmpty() }
        }
    }

    @Test
    fun `creates a character with skill proficiencies`() {
        mockMvc.post("/api/v1/characters") {
            contentType = MediaType.APPLICATION_JSON
            content = validRequest(
                name = "Lidda",
                level = 5,
                strength = 10,
                skills = """{ "PERCEPTION": "PROFICIENT", "STEALTH": "EXPERTISE", "ARCANA": "NONE" }""",
            )
        }.andExpect {
            status { isCreated() }
            jsonPath("$.skills.PERCEPTION.proficiency") { value("PROFICIENT") }
            jsonPath("$.skills.PERCEPTION.bonus") { value(3) }
            jsonPath("$.skills.STEALTH.proficiency") { value("EXPERTISE") }
            jsonPath("$.skills.STEALTH.bonus") { value(6) }
            jsonPath("$.skills.ARCANA.proficiency") { value("NONE") }
            jsonPath("$.passivePerception") { value(13) }
        }
    }

    @Test
    fun `rejects unknown skills`() {
        mockMvc.post("/api/v1/characters") {
            contentType = MediaType.APPLICATION_JSON
            content = validRequest(name = "Lidda", level = 1, strength = 10, skills = """{ "COOKING": "PROFICIENT" }""")
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `rejects unknown proficiencies`() {
        mockMvc.post("/api/v1/characters") {
            contentType = MediaType.APPLICATION_JSON
            content = validRequest(name = "Lidda", level = 1, strength = 10, skills = """{ "STEALTH": "MASTER" }""")
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `rejects invalid requests`() {
        mockMvc.post("/api/v1/characters") {
            contentType = MediaType.APPLICATION_JSON
            content = validRequest(name = "", level = 21, strength = 0)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `gets an existing character`() {
        val character = characterService.create(createCommand("Jozan", 2, abilityScores(wisdom = 15)))

        mockMvc.get("/api/v1/characters/{id}", character.id.value).andExpect {
            status { isOk() }
            jsonPath("$.name") { value("Jozan") }
            jsonPath("$.abilities.WISDOM.modifier") { value(2) }
        }
    }

    @Test
    fun `lists characters`() {
        characterService.create(createCommand("Alhandra", 4, abilityScores(charisma = 14)))

        mockMvc.get("/api/v1/characters").andExpect {
            status { isOk() }
            jsonPath("$") { isArray() }
            jsonPath("$[?(@.name == 'Alhandra')].proficiencyBonus") { value(hasItem(2)) }
            jsonPath("$[?(@.name == 'Alhandra')].abilities.CHARISMA.modifier") { value(hasItem(2)) }
        }
    }

    @Test
    fun `returns 404 when the character does not exist`() {
        mockMvc.get("/api/v1/characters/{id}", UUID.randomUUID()).andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `replaces the skill proficiencies of a character`() {
        val character = characterService.create(
            createCommand(
                "Soveliss",
                abilityScores = abilityScores(wisdom = 14),
                skillProficiencies = SkillProficiencies.of(mapOf(Skill.STEALTH to Proficiency.PROFICIENT)),
            ),
        )

        mockMvc.put("/api/v1/characters/{id}/skills", character.id.value) {
            contentType = MediaType.APPLICATION_JSON
            content = """{ "skills": { "PERCEPTION": "EXPERTISE" } }"""
        }.andExpect {
            status { isOk() }
            jsonPath("$.name") { value("Soveliss") }
            jsonPath("$.skills.STEALTH.proficiency") { value("NONE") }
            jsonPath("$.skills.PERCEPTION.proficiency") { value("EXPERTISE") }
            jsonPath("$.skills.PERCEPTION.bonus") { value(6) }
            jsonPath("$.passivePerception") { value(16) }
        }
    }

    @Test
    fun `returns 404 when updating skills of a character that does not exist`() {
        mockMvc.put("/api/v1/characters/{id}/skills", UUID.randomUUID()) {
            contentType = MediaType.APPLICATION_JSON
            content = """{ "skills": {} }"""
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `rejects skill updates without skills`() {
        val character = characterService.create(createCommand("Vadania"))

        mockMvc.put("/api/v1/characters/{id}/skills", character.id.value) {
            contentType = MediaType.APPLICATION_JSON
            content = "{}"
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `creates a multiclass character`() {
        mockMvc.post("/api/v1/characters") {
            contentType = MediaType.APPLICATION_JSON
            content = validRequest(
                name = "Soveliss",
                level = 0,
                strength = 10,
                classes = """[{ "class": "RANGER", "level": 5 }, { "class": "ROGUE", "level": 2 }]""",
            )
        }.andExpect {
            status { isCreated() }
            jsonPath("$.level") { value(7) }
            jsonPath("$.classes[0].class") { value("RANGER") }
            jsonPath("$.classes[1].class") { value("ROGUE") }
            jsonPath("$.hitDice[0].die") { value(10) }
            jsonPath("$.hitDice[1].die") { value(8) }
            jsonPath("$.hitDice[1].count") { value(2) }
            jsonPath("$.savingThrows.INTELLIGENCE.proficiency") { value("NONE") }
            jsonPath("$.classes[0].skillChoices.count") { value(3) }
            jsonPath("$.classes[1].skillChoices.count") { value(1) }
            jsonPath("$.classes[1].skillChoices.options.length()") { value(11) }
            jsonPath("$.proficiencies.armor") { value(contains("LIGHT", "MEDIUM", "SHIELDS")) }
            jsonPath("$.proficiencies.tools") { value(contains("THIEVES_TOOLS")) }
        }
    }

    @Test
    fun `returns the tool choices of the classes`() {
        mockMvc.post("/api/v1/characters") {
            contentType = MediaType.APPLICATION_JSON
            content =
                validRequest(
                    name = "Lidda",
                    level = 0,
                    strength = 10,
                    classes = """[{ "class": "BARD", "level": 1 }]""",
                )
        }.andExpect {
            status { isCreated() }
            jsonPath("$.classes[0].skillChoices.count") { value(3) }
            jsonPath("$.classes[0].skillChoices.options.length()") { value(18) }
            jsonPath("$.proficiencies.weapons") {
                value(contains("SIMPLE", "HAND_CROSSBOW", "LONGSWORD", "RAPIER", "SHORTSWORD"))
            }
            jsonPath("$.proficiencies.toolChoices[0].count") { value(3) }
            jsonPath("$.proficiencies.toolChoices[0].options") { value(contains("MUSICAL_INSTRUMENT")) }
        }
    }

    @Test
    fun `rejects characters without classes`() {
        mockMvc.post("/api/v1/characters") {
            contentType = MediaType.APPLICATION_JSON
            content = validRequest(name = "Lidda", level = 1, strength = 10, classes = "[]")
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `rejects unknown classes`() {
        mockMvc.post("/api/v1/characters") {
            contentType = MediaType.APPLICATION_JSON
            content =
                validRequest(
                    name = "Lidda",
                    level = 1,
                    strength = 10,
                    classes = """[{ "class": "ARTIFICER", "level": 1 }]""",
                )
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `rejects a total level above 20`() {
        mockMvc.post("/api/v1/characters") {
            contentType = MediaType.APPLICATION_JSON
            content = validRequest(
                name = "Lidda",
                level = 0,
                strength = 10,
                classes = """[{ "class": "FIGHTER", "level": 15 }, { "class": "ROGUE", "level": 6 }]""",
            )
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `rejects invalid max hit points`() {
        mockMvc.post("/api/v1/characters") {
            contentType = MediaType.APPLICATION_JSON
            content = validRequest(name = "Lidda", level = 1, strength = 10, maxHitPoints = 0)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `replaces the classes of a character`() {
        val character = characterService.create(
            createCommand("Fjör", level = 15, abilityScores = abilityScores(wisdom = 18)),
        )

        mockMvc.put("/api/v1/characters/{id}/classes", character.id.value) {
            contentType = MediaType.APPLICATION_JSON
            content = """{ "classes": [{ "class": "CLERIC", "level": 15 }] }"""
        }.andExpect {
            status { isOk() }
            jsonPath("$.level") { value(15) }
            jsonPath("$.classes.length()") { value(1) }
            jsonPath("$.classes[0].class") { value("CLERIC") }
            jsonPath("$.hitDice[0].die") { value(8) }
            jsonPath("$.savingThrows.WISDOM.proficiency") { value("PROFICIENT") }
            jsonPath("$.savingThrows.WISDOM.bonus") { value(9) }
        }
    }

    @Test
    fun `rejects class updates without classes`() {
        val character = characterService.create(createCommand("Vadania"))

        mockMvc.put("/api/v1/characters/{id}/classes", character.id.value) {
            contentType = MediaType.APPLICATION_JSON
            content = """{ "classes": [] }"""
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `returns 404 when updating classes of a character that does not exist`() {
        mockMvc.put("/api/v1/characters/{id}/classes", UUID.randomUUID()) {
            contentType = MediaType.APPLICATION_JSON
            content = """{ "classes": [{ "class": "BARD", "level": 1 }] }"""
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `updates the max hit points of a character`() {
        val character = characterService.create(createCommand("Krusk", characterClass = CharacterClass.BARBARIAN))

        mockMvc.put("/api/v1/characters/{id}/hit-points", character.id.value) {
            contentType = MediaType.APPLICATION_JSON
            content = """{ "maxHitPoints": 15 }"""
        }.andExpect {
            status { isOk() }
            jsonPath("$.maxHitPoints") { value(15) }
            jsonPath("$.classes[0].class") { value("BARBARIAN") }
        }
    }

    @Test
    fun `rejects invalid hit point updates`() {
        val character = characterService.create(createCommand("Krusk"))

        mockMvc.put("/api/v1/characters/{id}/hit-points", character.id.value) {
            contentType = MediaType.APPLICATION_JSON
            content = """{ "maxHitPoints": 1000 }"""
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `returns 404 when updating hit points of a character that does not exist`() {
        mockMvc.put("/api/v1/characters/{id}/hit-points", UUID.randomUUID()) {
            contentType = MediaType.APPLICATION_JSON
            content = """{ "maxHitPoints": 10 }"""
        }.andExpect {
            status { isNotFound() }
        }
    }

    private fun validRequest(
        name: String,
        level: Int,
        strength: Int,
        skills: String? = null,
        classes: String = """[{ "class": "FIGHTER", "level": $level }]""",
        maxHitPoints: Int = 44,
    ) = """
        {
          "name": "$name",
          "classes": $classes,
          "maxHitPoints": $maxHitPoints,
          "abilityScores": {
            "strength": $strength, "dexterity": 10, "constitution": 10,
            "intelligence": 10, "wisdom": 10, "charisma": 10
          }${skills?.let { ", \"skills\": $it" }.orEmpty()}
        }
    """.trimIndent()
}
