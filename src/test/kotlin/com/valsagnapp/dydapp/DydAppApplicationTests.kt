package com.valsagnapp.dydapp

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@SpringBootTest
@Import(TestcontainersConfiguration::class)
class DydAppApplicationTests {
    @Test
    fun `context loads`() {
        // Falla si no levanta el contexto: wiring de beans, config o migraciones de Flyway.
    }
}
