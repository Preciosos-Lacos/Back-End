package com.lacos_preciosos.preciososLacos.model

import com.lacos_preciosos.preciososLacos.dto.produto.CadastroProdutoDTO
import jakarta.persistence.*

@Entity
data class Produto(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idProduto: Int? = null,

    var tamanho: String? = "",

    var cor: Int? = null,

    @Column(name = "tipo_laco")
    var tipoLaco: Int? = null,

    var preco: Double = 0.0,

    @ManyToOne
    var modelo: Modelo? = null,

    val nome: String = "",

) {

    constructor(dto: CadastroProdutoDTO) : this(
        null,
        dto.tamanho,
        dto.cor,
        dto.tipoLaco,
        dto.preco,
        null,
        dto.nome ?: ""
    )


}