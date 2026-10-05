package com.valsagnapp.dydapp.config

import com.valsagnapp.dydapp.application.port.outbound.CharacterRepository
import com.valsagnapp.dydapp.application.service.CharacterService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// Los servicios de aplicación no conocen Spring: se registran acá como beans.
@Configuration(proxyBeanMethods = false)
class ApplicationConfig {
    @Bean
    fun characterService(characterRepository: CharacterRepository) = CharacterService(characterRepository)
}
