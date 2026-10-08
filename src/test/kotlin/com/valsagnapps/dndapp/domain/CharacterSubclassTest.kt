package com.valsagnapps.dndapp.domain

import com.valsagnapps.dndapp.domain.CharacterClass.CLERIC
import com.valsagnapps.dndapp.domain.CharacterClass.FIGHTER
import com.valsagnapps.dndapp.domain.CharacterClass.WIZARD
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class CharacterSubclassTest {
    private val lifeCleric = character(classes = listOf(ClassLevel(CLERIC, 5, Subclass.LIFE)))

    @Test
    fun `chooses the subclass of one of its classes`() {
        val character = character(classes = listOf(ClassLevel(FIGHTER, 3), ClassLevel(WIZARD, 2)))

        val updated = character.withSubclass(WIZARD, Subclass.EVOCATION)

        assertEquals(listOf(null, Subclass.EVOCATION), updated.classes.map { it.subclass })
    }

    @Test
    fun `removes the subclass with null`() {
        assertNull(lifeCleric.withSubclass(CLERIC, null).classes.single().subclass)
    }

    @Test
    fun `rejects a subclass for a class the character does not have`() {
        assertFailsWith<IllegalArgumentException> { lifeCleric.withSubclass(WIZARD, Subclass.EVOCATION) }
    }

    @Test
    fun `rejects a subclass of another class`() {
        assertFailsWith<IllegalArgumentException> { lifeCleric.withSubclass(CLERIC, Subclass.EVOCATION) }
    }

    @Test
    fun `rejects a subclass below the level to choose it`() {
        val character = character(classes = listOf(ClassLevel(WIZARD, 1)))

        assertFailsWith<IllegalArgumentException> { character.withSubclass(WIZARD, Subclass.EVOCATION) }
    }

    @Test
    fun `keeps the subclass when the class levels up`() {
        val updated = lifeCleric.withClasses(listOf(ClassLevel(CLERIC, 6)))

        assertEquals(ClassLevel(CLERIC, 6, Subclass.LIFE), updated.classes.single())
    }

    @Test
    fun `keeps the subclass of classes that stay when adding another one`() {
        val updated = lifeCleric.withClasses(listOf(ClassLevel(CLERIC, 5), ClassLevel(FIGHTER, 1)))

        assertEquals(listOf(Subclass.LIFE, null), updated.classes.map { it.subclass })
    }

    @Test
    fun `removes the subclass when the class changes`() {
        val updated = lifeCleric.withClasses(listOf(ClassLevel(FIGHTER, 5)))

        assertEquals(ClassLevel(FIGHTER, 5), updated.classes.single())
    }

    @Test
    fun `removes the subclass when the level drops below the level to choose it`() {
        val evoker = character(classes = listOf(ClassLevel(WIZARD, 4, Subclass.EVOCATION)))

        val updated = evoker.withClasses(listOf(ClassLevel(WIZARD, 1)))

        assertEquals(ClassLevel(WIZARD, 1), updated.classes.single())
    }

    @Test
    fun `uses the subclass of the new classes when they bring one`() {
        val updated = lifeCleric.withClasses(listOf(ClassLevel(CLERIC, 5, Subclass.WAR)))

        assertEquals(Subclass.WAR, updated.classes.single().subclass)
    }
}
