package com.valsagnapp.dydapp.domain

class CharacterNotFoundException(id: CharacterId) : RuntimeException("Character ${id.value} not found")
