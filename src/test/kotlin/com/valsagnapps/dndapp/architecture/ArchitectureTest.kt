package com.valsagnapps.dndapp.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.architecture.KoArchitectureCreator.assertArchitecture
import com.lemonappdev.konsist.api.architecture.Layer
import com.lemonappdev.konsist.api.verify.assertFalse
import kotlin.test.Test

class ArchitectureTest {
    private val production = Konsist.scopeFromProduction()

    @Test
    fun `dependencies point inwards`() {
        production.assertArchitecture {
            val domain = Layer("Domain", "com.valsagnapps.dndapp.domain..")
            val application = Layer("Application", "com.valsagnapps.dndapp.application..")
            val adapter = Layer("Adapter", "com.valsagnapps.dndapp.adapter..")

            domain.dependsOnNothing()
            application.dependsOn(domain)
            adapter.dependsOn(application, domain)
        }
    }

    @Test
    fun `domain and application do not depend on frameworks`() {
        production
            .files
            .filter { it.packagee?.name?.let(::isCore) == true }
            .assertFalse { file ->
                file.imports.any { import ->
                    FRAMEWORK_PACKAGES.any { import.name.startsWith(it) }
                }
            }
    }

    @Test
    fun `web DTOs stay in the web adapter`() {
        production
            .classes()
            .filter { it.name.endsWith("Request") || it.name.endsWith("Response") || it.name.endsWith("Dto") }
            .assertFalse { it.resideOutsidePackage("com.valsagnapps.dndapp.adapter.inbound.web..") }
    }

    @Test
    fun `JPA entities stay in the persistence adapter`() {
        production
            .classes()
            .filter { it.hasAnnotationWithName("Entity") }
            .assertFalse { it.resideOutsidePackage("com.valsagnapps.dndapp.adapter.outbound.persistence..") }
    }

    private fun isCore(packageName: String) = CORE_PACKAGES.any { packageName.startsWith(it) }

    private companion object {
        val CORE_PACKAGES = listOf("com.valsagnapps.dndapp.domain", "com.valsagnapps.dndapp.application")
        val FRAMEWORK_PACKAGES = listOf("org.springframework", "jakarta.persistence", "jakarta.validation")
    }
}
