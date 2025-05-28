package com.lacos_preciosos.preciososLacos.model

import com.lacos_preciosos.preciososLacos.dto.produto.CadastroProdutoDTO
import jakarta.persistence.*

@Entity
data class Produto(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idProduto: Int? = null,

    var tamanho: String = "",

    var cor: String = "",

    @Column(name = "tipo_laco")
    var tipoLaco: String = "",

    var acabamento: String = "",

    var preco: Double = 0.0,

    @ManyToOne
    var modelo: Modelo? = null

) {

    constructor(dto: CadastroProdutoDTO) : this(
        null,
        dto.tamanho,
        dto.cor,
        dto.material,
        dto.acabamento,
        dto.preco,
        null
    )


}