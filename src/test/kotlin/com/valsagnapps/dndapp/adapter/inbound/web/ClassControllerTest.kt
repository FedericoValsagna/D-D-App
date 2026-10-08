package com.valsagnapps.dndapp.adapter.inbound.web

import com.valsagnapps.dndapp.application.service.ClassCatalogService
import org.hamcrest.Matchers.contains
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import kotlin.test.Test

@WebMvcTest(ClassController::class)
@Import(ClassControllerTest.Config::class)
class ClassControllerTest {
    @TestConfiguration
    class Config {
        @Bean
        fun classCatalogService() = ClassCatalogService()
    }

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `lists the classes with their subclasses`() {
        mockMvc.get("/api/v1/classes").andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(12) }
            jsonPath("$[0].class") { value("BARBARIAN") }
            jsonPath("$[0].hitDie") { value(12) }
            jsonPath("$[0].source") { value("PHB") }
            jsonPath("$[2].class") { value("CLERIC") }
            jsonPath("$[2].subclassLevel") { value(1) }
            jsonPath("$[2].subclasses.length()") { value(7) }
            jsonPath("$[2].subclasses[1].id") { value("LIFE") }
            jsonPath("$[2].subclasses[1].name") { value("Life Domain") }
            jsonPath("$[2].subclasses[1].source") { value("PHB") }
            jsonPath("$[3].subclasses[*].id") { value(contains("LAND", "MOON")) }
        }
    }
}
