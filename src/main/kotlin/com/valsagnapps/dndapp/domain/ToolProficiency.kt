package com.valsagnapps.dndapp.domain

enum class ToolProficiency {
    HERBALISM_KIT,
    THIEVES_TOOLS,
}

// Grupos de herramientas entre los que el jugador elige (ej. "tres instrumentos musicales").
enum class ToolCategory {
    ARTISANS_TOOLS,
    MUSICAL_INSTRUMENT,
}

// Elección pendiente: `count` herramientas de alguno de los grupos de `options`. Por ahora no se persiste qué eligió.
data class ToolChoice(val count: Int, val options: Set<ToolCategory>) {
    init {
        require(count > 0) { "a tool choice must grant at least one tool" }
        require(options.isNotEmpty()) { "a tool choice must have options" }
    }
}
