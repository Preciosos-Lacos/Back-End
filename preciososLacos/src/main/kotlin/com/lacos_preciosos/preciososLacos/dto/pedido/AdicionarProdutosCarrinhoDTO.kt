package com.lacos_preciosos.preciososLacos.dto.pedido

data class AdicionarProdutosCarrinhoRequest(
    val idUsuario: Int,
    val produtos: List<ProdutoCarrinhoDTO>
)

data class ProdutoCarrinhoDTO(
    val idProduto: Int,
    val quantidade: Int = 1
)
