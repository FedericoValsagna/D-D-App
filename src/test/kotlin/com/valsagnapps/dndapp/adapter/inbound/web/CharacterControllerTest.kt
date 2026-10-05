package com.valsagnapps.dndapp.adapter.inbound.web

import com.valsagnapps.dndapp.application.port.inbound.CreateCharacterCommand
import com.valsagnapps.dndapp.application.service.CharacterService
import com.valsagnapps.dndapp.application.service.InMemoryCharacterRepository
import com.valsagnapps.dndapp.domain.Proficiency
import com.valsagnapps.dndapp.domain.Skill
import com.valsagnapps.dndapp.domain.SkillProficiencies
import com.valsagnapps.dndapp.domain.abilityScores
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
            jsonPath("$.proficiencyBonus") { value(3) }
            jsonPath("$.abilities.STRENGTH.score") { value(17) }
            jsonPath("$.abilities.STRENGTH.modifier") { value(3) }
            jsonPath("$.skills.ATHLETICS.ability") { value("STRENGTH") }
            jsonPath("$.skills.ATHLETICS.proficiency") { value("NONE") }
            jsonPath("$.skills.ATHLETICS.bonus") { value(3) }
            jsonPath("$.skills.length()") { value(18) }
            jsonPath("$.passivePerception") { value(10) }
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
        val character = characterService.create(CreateCharacterCommand("Jozan", 2, abilityScores(wisdom = 15)))

        mockMvc.get("/api/v1/characters/{id}", character.id.value).andExpect {
            status { isOk() }
            jsonPath("$.name") { value("Jozan") }
            jsonPath("$.abilities.WISDOM.modifier") { value(2) }
        }
    }

    @Test
    fun `lists characters`() {
        characterService.create(CreateCharacterCommand("Alhandra", 4, abilityScores(charisma = 14)))

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
            CreateCharacterCommand(
                "Soveliss",
                1,
                abilityScores(wisdom = 14),
                SkillProficiencies.of(mapOf(Skill.STEALTH to Proficiency.PROFICIENT)),
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
        val character = characterService.create(CreateCharacterCommand("Vadania", 1, abilityScores()))

        mockMvc.put("/api/v1/characters/{id}/skills", character.id.value) {
            contentType = MediaType.APPLICATION_JSON
            content = "{}"
        }.andExpect {
            status { isBadRequest() }
        }
    }

    private fun validRequest(name: String, level: Int, strength: Int, skills: String? = null) =
        """
        {
          "name": "$name",
          "level": $level,
          "abilityScores": {
            "strength": $strength, "dexterity": 10, "constitution": 10,
            "intelligence": 10, "wisdom": 10, "charisma": 10
          }${skills?.let { ", \"skills\": $it" }.orEmpty()}
        }
        """.trimIndent()
}
