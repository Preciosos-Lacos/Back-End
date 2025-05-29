package com.lacos_preciosos.preciososLacos.dto.favorito

data class AtualizacaoFavoritoDTO(
    val usuarioIdAntigo: Int,
    val produtoIdAntigo: Int,
    val usuarioIdNovo: Int,
    val produtoIdNovo: Int
)
{

}
