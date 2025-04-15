package com.lacos_preciosos.preciososLacos.model

import jakarta.persistence.*

@Entity
data class Produto(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idProduto: Int?,

    var tamanho: String,

    var cor: String,

    @Column(name = "tipo_laco")
    var tipoLaco: String,

    var acabamento: String,

    var preco: Double
) {
}