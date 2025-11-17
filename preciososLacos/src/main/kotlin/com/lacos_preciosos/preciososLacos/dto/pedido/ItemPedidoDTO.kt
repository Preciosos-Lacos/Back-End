package com.lacos_preciosos.preciososLacos.dto.pedido

data class ItemPedidoDTO(
    val sku: String?,
    val nome: String?,
    val quantidade: Int,
    val preco: Double
)
