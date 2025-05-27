package com.lacos_preciosos.preciososLacos.dto

import com.lacos_preciosos.preciososLacos.model.Pedido

data class DadosDetalhePedido(var total: Double,
                              var formaPagamento: String)
{
    constructor(pedido: Pedido) : this(
        pedido.total,
        pedido.formaPagamento.toString()
    )
}

