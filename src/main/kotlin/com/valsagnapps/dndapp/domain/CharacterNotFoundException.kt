package com.valsagnapps.dndapp.domain

class CharacterNotFoundException(id: CharacterId) : RuntimeException("Character ${id.value} not found")
