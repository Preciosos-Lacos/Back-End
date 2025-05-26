package com.lacos_preciosos.preciososLacos.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank

@Entity
data class Caracteristica(

    @Id
    @field:NotBlank
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idCaracteristica: Int? = null,

    @field:NotBlank
    var descricao: String? = null,
) {
}