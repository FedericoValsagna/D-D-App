package com.valsagnapps.dndapp.domain

// Cuántas veces se suma el bonus de competencia: expertise lo duplica.
enum class Proficiency(val multiplier: Int) {
    NONE(0),
    PROFICIENT(1),
    EXPERTISE(2),
}
