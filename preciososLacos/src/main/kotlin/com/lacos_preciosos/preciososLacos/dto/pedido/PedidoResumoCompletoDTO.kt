package com.lacos_preciosos.preciososLacos.dto.pedido

import com.lacos_preciosos.preciososLacos.dto.produto.ProdutoDTO
import com.lacos_preciosos.preciososLacos.tipos.TipoPagamento
import java.math.BigDecimal
import java.time.LocalDate


data class PedidoResumoCompletoDTO(
    val idPedido: Long,
    val nomeCliente: String?,
    val telefone: String?,
    val dataPedido: LocalDate?,
    val previsaoEntrega: String?,
    val total: BigDecimal,
    val formaPagamento: TipoPagamento?,
    val statusPagamento: String?,
    val statusPedido: String?,
    val produtos: List<ProdutoDTO>
)
