package com.valsagnapp.dndapp.domain

import java.util.UUID

@JvmInline
value class CharacterId(val value: UUID) {
    companion object {
        fun new() = CharacterId(UUID.randomUUID())
    }
}
