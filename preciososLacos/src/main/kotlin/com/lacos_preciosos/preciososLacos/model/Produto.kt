package com.lacos_preciosos.preciososLacos.model

import com.lacos_preciosos.preciososLacos.dto.produto.CadastroProdutoDTO
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

    constructor(dto: CadastroProdutoDTO) : this(
        null,
        dto.tamanho,
        dto.cor,
        dto.material,
        dto.acabamento,
        dto.preco
    )

}