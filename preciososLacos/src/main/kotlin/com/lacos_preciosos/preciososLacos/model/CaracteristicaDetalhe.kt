package com.lacos_preciosos.preciososLacos.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
data class CaracteristicaDetalhe(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:NotBlank
    var idCaracteristicaDetalhe: Int? = null,
    
    @field:NotBlank
    var descricao: String? = null,
    
    @ManyToOne
    var caracteristica: Caracteristica? = null

    
) {
}