package com.valsagnapps.dndapp.application.service

import com.valsagnapps.dndapp.domain.CharacterClass
import kotlin.test.Test
import kotlin.test.assertEquals

class ClassCatalogServiceTest {
    @Test
    fun `lists every class in order`() {
        assertEquals(CharacterClass.entries, ClassCatalogService().listClasses())
    }
}
