package com.lacos_preciosos.preciososLacos.model

import com.lacos_preciosos.preciososLacos.dto.pedido.CadastroItemPedidoDTO
import com.lacos_preciosos.preciososLacos.tipos.TipoPagamento
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.LocalDate

@Entity
data class Pedido(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idPedido: Int? = null,

    var dataPedido: LocalDate = LocalDate.now(),

    var total: Double = 0.0,

    var formaPagamento: TipoPagamento? = null,

    @ManyToOne
    var usuario: Usuario? = null,

    @ManyToOne
    var statusPedido: StatusPedido? = null,

    @ManyToOne
    var statusPagamento: StatusPagamento? = null
) {
    constructor(dto: CadastroItemPedidoDTO) : this(
        null,
        LocalDate.now(),
        dto.total,
        TipoPagamento.getTipoPagamento(dto.formaPagamento),
        null,
        null,
        null
    )
}