package com.lacos_preciosos.preciososLacos.dto.produto

data class ProdutoDTO(
    val idProduto: Int?,
    val nome: String,
    val colecao: String?,      // do modelo.nomeModelo
    val tamanho: String?,
    val tipoLaco: String?,
    val foto: String?,         // url da foto (modelo.foto)
    val preco: Double?
)
