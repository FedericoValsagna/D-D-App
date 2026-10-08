package com.valsagnapps.dndapp.adapter.inbound.web

import com.valsagnapps.dndapp.application.port.inbound.ListClassesUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/classes")
class ClassController(private val listClasses: ListClassesUseCase) {
    @GetMapping
    fun list(): List<ClassResponse> = listClasses.listClasses().map(ClassResponse::from)
}
