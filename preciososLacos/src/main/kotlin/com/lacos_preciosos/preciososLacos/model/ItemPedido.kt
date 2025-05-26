package com.lacos_preciosos.preciososLacos.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
data class ItemPedido(
    @Id
    @ManyToOne
    @field:NotBlank
    var idPedido: Pedido? = null,

    @Id
    @ManyToOne
    @field:NotBlank
    var idProduto: Produto? = null,

    var quantidade: Int? = null
){

}