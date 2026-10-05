package com.valsagnapps.dndapp.adapter.inbound.web

import com.valsagnapps.dndapp.domain.CharacterNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ApiExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(CharacterNotFoundException::class)
    fun handleNotFound(ex: CharacterNotFoundException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.message)

    // Reglas de dominio violadas (los require() de los modelos).
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleInvalidArgument(ex: IllegalArgumentException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.message)
}
