package com.lacos_preciosos.preciososLacos.dto.pedido

data class CadastroPedidoDTO(
    var total: Double,
    var formaPagamento: String,
    var idUsuario: Int,
    var listaIdProdutos: List<Int>
) {


}
