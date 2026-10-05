package com.valsagnapp.dndapp.domain

class CharacterNotFoundException(id: CharacterId) : RuntimeException("Character ${id.value} not found")
