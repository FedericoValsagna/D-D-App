package com.valsagnapp.dndapp.adapter.outbound.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CharacterJpaRepository : JpaRepository<CharacterEntity, UUID>
