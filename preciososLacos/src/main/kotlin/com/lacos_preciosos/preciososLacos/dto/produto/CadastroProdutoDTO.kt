package com.lacos_preciosos.preciososLacos.dto.produto

data class CadastroProdutoDTO(
    val nome: String,
    val tamanho: String,
    val material: String,
    val cor: Int?,
    val acabamento: Int?,
    val preco: Double,
    val idModelo: Int
) {
}