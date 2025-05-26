package com.lacos_preciosos.preciososLacos.dto.pedido

data class CadastroItemPedidoDTO(
    var idPedido: Int,
    var idProduto: Int,
    var quantidade: Int,
    var idUsuario: Int) {
}

