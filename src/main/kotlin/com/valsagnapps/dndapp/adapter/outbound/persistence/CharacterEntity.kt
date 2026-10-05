package com.valsagnapps.dndapp.adapter.outbound.persistence

import com.valsagnapps.dndapp.domain.Proficiency
import com.valsagnapps.dndapp.domain.Skill
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapKeyColumn
import jakarta.persistence.MapKeyEnumerated
import jakarta.persistence.OrderColumn
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "characters")
class CharacterEntity(
    @Id val id: UUID,
    val name: String,
    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int,
    val maxHitPoints: Int,
    // EAGER porque open-in-view está apagado: el mapeo a dominio pasa fuera de la transacción.
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "character_skills", joinColumns = [JoinColumn(name = "character_id")])
    @MapKeyColumn(name = "skill")
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "proficiency")
    @Enumerated(EnumType.STRING)
    val skills: MutableMap<Skill, Proficiency>,
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "character_classes", joinColumns = [JoinColumn(name = "character_id")])
    @OrderColumn(name = "position")
    val classes: MutableList<ClassLevelEmbeddable>,
)
