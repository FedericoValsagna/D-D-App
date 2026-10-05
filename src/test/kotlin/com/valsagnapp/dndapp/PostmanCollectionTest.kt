package com.valsagnapp.dndapp

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import org.yaml.snakeyaml.Yaml
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

// Cada endpoint de /api tiene que tener su request en la colección de Postman (postman/collections), y viceversa.
@SpringBootTest
@Import(TestcontainersConfiguration::class)
class PostmanCollectionTest {
    @Autowired
    @Qualifier("requestMappingHandlerMapping")
    lateinit var handlerMapping: RequestMappingHandlerMapping

    @Test
    fun `postman collection matches the API endpoints`() {
        val serverEndpoints = handlerMapping.handlerMethods.keys
            .flatMap { info ->
                info.methodsCondition.methods.flatMap { method -> info.patternValues.map { "$method $it" } }
            }
            .filter { it.substringAfter(" ").startsWith("/api/") }
            .map(::normalize)
            .toSortedSet()

        val postmanEndpoints = File("postman/collections")
            .walk()
            .filter { it.name.endsWith(".request.yaml") }
            .map { Yaml().load<Map<String, Any>>(it.readText()) }
            .map { "${it["method"]} ${(it["url"] as String).removePrefix("{{baseUrl}}")}" }
            .filter { it.substringAfter(" ").startsWith("/api/") }
            .map(::normalize)
            .toSortedSet()

        assertEquals(serverEndpoints, postmanEndpoints, "Agregar o quitar requests en postman/collections")
    }

    // Variables de path de Spring ({id}) y de Postman ({{characterId}}) se comparan como "{}".
    private fun normalize(endpoint: String) = endpoint.replace(Regex("\\{\\{?[^}]+}}?"), "{}")
}
