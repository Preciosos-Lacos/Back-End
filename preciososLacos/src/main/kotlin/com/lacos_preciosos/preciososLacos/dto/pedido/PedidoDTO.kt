package com.lacos_preciosos.preciososLacos.dto.pedido

data class PedidoDTO(
    val id: Int?,
    val numeroPedido: String?,
    val dataPedido: String?,
    val valorTotal: Double,
    val formaPagamento: String?,
    val statusPagamento: String?,
    val statusPedido: String?,
    val cliente: ClienteResumoDTO?,
    val enderecoEntrega: String?,
    val itens: List<ItemPedidoDTO>,
    val modelos: List<String>?,
    val tamanho: String?,
    val cores: String?,
)

data class ClienteResumoDTO(
    val nome: String?,
    val telefone: String?,
    val email: String?
)
