package com.lacos_preciosos.preciososLacos.dto.produto

data class CadastroProdutoDTO(
    val nome: String,
    val tamanho: String,
    val material: String,
    val cor: String,
    val acabamento: String,
    val preco: Double
) {
}