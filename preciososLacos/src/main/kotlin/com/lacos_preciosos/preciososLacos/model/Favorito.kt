package com.lacos_preciosos.preciososLacos.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
data class Favorito(
    @Id
    @field:NotBlank
    @ManyToOne
    var idProduto: Produto? = null,

    @Id
    @field:NotBlank
    @ManyToOne
    var idUsuario: Usuario? = null,

    @field:NotBlank
    var favorito: String? = null,
) {
}