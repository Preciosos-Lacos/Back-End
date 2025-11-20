package com.lacos_preciosos.preciososLacos.dto.produto

import com.lacos_preciosos.preciososLacos.model.Produto

data class DadosDetalheProduto(
    var idProduto: Int?,
    var nome: String,
    var tamanho: String,
    var material: String,
    var cor: Int?,
    var tipoLaco: String,
    var acabamento: Int?,
    var preco: Double,
    var idModelo: Int?
) {
    constructor(produto: Produto) : this(
        produto.idProduto,
        produto.nome,
        produto.tamanho,
        produto.material,
        produto.cor,
        produto.tipoLaco,
        produto.acabamento,
        produto.preco,
        produto.modelo?.idModelo
    )
}