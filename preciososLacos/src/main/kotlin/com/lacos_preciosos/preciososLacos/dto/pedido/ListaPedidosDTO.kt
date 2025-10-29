package com.lacos_preciosos.preciososLacos.dto.pedido

data class ListaPedidosDTO( val nomeCliente: String,
                            val telefone: String,
                            val dataPedido: String,
                            val total: Double,
                            val formaPagamento: String,
                            val statusPagamento: String,
                            val statusPedido: String)
