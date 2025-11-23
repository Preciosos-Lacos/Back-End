package com.lacos_preciosos.preciososLacos.dto.pedido

data class ListaPedidosDTO(
    val idPedido: Int,
    val data: String,
    val statusPedido: String?,
    val statusPagamento: String?,
    val total: Double,
    val itens: List<ItemPedidoDetalheDTO>
)

data class ItemPedidoDetalheDTO(
    val idProduto: Int,
    val nome: String?,
    val modelo: String?,
    val quantidade: Int,
    val preco: Double?,
    val imagens: List<String>,
    val caracteristicas: List<CaracteristicaItemDTO>
)

data class CaracteristicaItemDTO(
    val nome: String?, // nome da caracteristica (ex: Tamanho, Tipo de Fecho)
    val detalhe: String? // detalhe selecionado (ex: M, Bico de Pato)
)

