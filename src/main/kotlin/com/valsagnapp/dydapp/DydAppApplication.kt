package com.valsagnapp.dydapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DydAppApplication

fun main(args: Array<String>) {
    runApplication<DydAppApplication>(*args)
}
