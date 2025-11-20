package com.lacos_preciosos.preciososLacos.dto.produto

data class CadastroProdutoDTO(
    val nome: String,
    val tamanho: String,
    val cor: Int,
    val tipoLaco: Int,
    val preco: Double,
    val idModelo: Int
) {
}