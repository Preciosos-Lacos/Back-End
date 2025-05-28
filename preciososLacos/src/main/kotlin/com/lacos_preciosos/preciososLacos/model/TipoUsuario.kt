package com.lacos_preciosos.preciososLacos.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank

@Entity
data class TipoUsuario(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idTipoUsuario: Int? = null,

    @field:NotBlank
    var tipoUsuario: String? = null

) {
}