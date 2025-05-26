package com.lacos_preciosos.preciososLacos.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.validation.constraints.NotBlank

@Entity
data class ModeloCaracteristicaDetalhe(
    @Id
    @field:NotBlank
    @ManyToOne
    var fkModelo: Modelo? = null,


    @field:NotBlank
    @ManyToOne
    var caracteristica: CaracteristicaDetalhe? = null
) {
}