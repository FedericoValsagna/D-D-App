package com.valsagnapp.dndapp.domain

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CharacterTest {
    @ParameterizedTest
    @CsvSource("1, 2", "4, 2", "5, 3", "8, 3", "9, 4", "13, 5", "17, 6", "20, 6")
    fun `proficiency bonus grows every four levels`(level: Int, expectedBonus: Int) {
        assertEquals(expectedBonus, character(level = level).proficiencyBonus)
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 21])
    fun `rejects levels out of range`(level: Int) {
        assertFailsWith<IllegalArgumentException> { character(level = level) }
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "   "])
    fun `rejects blank names`(name: String) {
        assertFailsWith<IllegalArgumentException> { character(name = name) }
    }

    @Test
    fun `rejects names that are too long`() {
        assertFailsWith<IllegalArgumentException> { character(name = "a".repeat(Character.MAX_NAME_LENGTH + 1)) }
    }
}
