package com.valsagnapps.dndapp.domain

// Feature de clase o subclase que se gana al llegar a `level` (nivel de esa clase, no del personaje).
// `id` es estable: de él van a colgar los recursos con usos (slice 7). Lo que mejora con el nivel (usos, daño, ...)
// va en una sola feature, con el detalle en el resumen.
// `summary` es un resumen propio (lo que muestra la app); `srdText`, el texto completo del SRD 5.1 cuando la feature
// es del SRD (lo que no es SRD no puede llevar el texto del libro).
data class ClassFeature(
    val id: String,
    val name: String,
    val level: Int,
    val summary: String,
    val srdText: String? = null,
    val source: Source = Source.PHB,
)
