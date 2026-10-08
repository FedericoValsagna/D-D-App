package com.valsagnapps.dndapp.config

import com.valsagnapps.dndapp.application.port.outbound.CharacterRepository
import com.valsagnapps.dndapp.application.service.CharacterService
import com.valsagnapps.dndapp.application.service.ClassCatalogService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// Los servicios de aplicación no conocen Spring: se registran acá como beans.
@Configuration(proxyBeanMethods = false)
class ApplicationConfig {
    @Bean
    fun characterService(characterRepository: CharacterRepository) = CharacterService(characterRepository)

    @Bean
    fun classCatalogService() = ClassCatalogService()
}
