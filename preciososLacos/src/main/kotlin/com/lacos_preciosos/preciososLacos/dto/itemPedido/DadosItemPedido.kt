package com.lacos_preciosos.preciososLacos.dto.itemPedido

import com.lacos_preciosos.preciososLacos.model.ItemPedido
import lombok.NoArgsConstructor

@NoArgsConstructor
data class DadosItemPedido(
    var tamanho: String,
    var cor: String,
    var tipoLaco: String,
    var acabamento: String,
    var preco: Double

) {
    constructor(itemPedido: ItemPedido) : this(
        itemPedido.idItemPedido.toString(),
        itemPedido.pedido?.toString() ?: "Pedido não encontrado",
        itemPedido.quantidade.toString(),
        itemPedido.usuario?.toString() ?: "Usuário não encontrado",
        itemPedido.produto?.preco ?: 0.0
    )
}