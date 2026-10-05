package com.valsagnapp.dydapp.adapter.inbound.web

import org.springframework.http.HttpStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class ApiExceptionHandlerTest {
    private val handler = ApiExceptionHandler()

    @Test
    fun `maps domain rule violations to 400`() {
        val problem = handler.handleInvalidArgument(IllegalArgumentException("level must be between 1 and 20"))

        assertEquals(HttpStatus.BAD_REQUEST.value(), problem.status)
        assertEquals("level must be between 1 and 20", problem.detail)
    }
}
