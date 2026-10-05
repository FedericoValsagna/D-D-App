package com.valsagnapp.dndapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DndAppApplication

fun main(args: Array<String>) {
    runApplication<DndAppApplication>(*args)
}
