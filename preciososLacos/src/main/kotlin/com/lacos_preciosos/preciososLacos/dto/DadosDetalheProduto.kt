package com.lacos_preciosos.preciososLacos.dto

import com.lacos_preciosos.preciososLacos.model.Produto
import lombok.NoArgsConstructor

@NoArgsConstructor
data class DadosDetalheProduto(
    var tamanho: String,
    var cor: String,
    var tipoLaco: String,
    var acabamento: String,
    var preco: Double

) {
    constructor(produto: Produto) : this(
        produto.tamanho,
        produto.cor,
        produto.tipoLaco,
        produto.acabamento,
        produto.preco
    )

}