package com.lacos_preciosos.preciososLacos.dto.favorito

    data class DadosDetalheFavorito(
        val idFavorito: Int,
        val nome: String,
        val material: String,
        val tamanho: String,
        val cor: String,
        val tipoLaco: String,
        val acabamento: String,
        val preco: Double,
        val idModelo: Int,
        val idUsuario: Int? = null
)
{

}