package com.valsagnapp.dndapp.adapter.inbound.web

import com.valsagnapp.dndapp.application.port.inbound.CreateCharacterCommand
import com.valsagnapp.dndapp.application.service.CharacterService
import com.valsagnapp.dndapp.application.service.InMemoryCharacterRepository
import com.valsagnapp.dndapp.domain.abilityScores
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
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
    fun `returns 404 when the character does not exist`() {
        mockMvc.get("/api/v1/characters/{id}", UUID.randomUUID()).andExpect {
            status { isNotFound() }
        }
    }

    private fun validRequest(name: String, level: Int, strength: Int) =
        """
        {
          "name": "$name",
          "level": $level,
          "abilityScores": {
            "strength": $strength, "dexterity": 10, "constitution": 10,
            "intelligence": 10, "wisdom": 10, "charisma": 10
          }
        }
        """.trimIndent()
}
