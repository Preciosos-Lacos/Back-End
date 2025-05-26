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

    var dataPedido: LocalDate,

    var total: Double,

    var formaPagamento: TipoPagamento,

    @ManyToOne
    var usuario: Usuario?,

    @ManyToOne
    var statusPedido: StatusPedido?,

    @ManyToOne
    var statusPagamento: StatusPagamento?
) {
    constructor(dto: CadastroItemPedidoDTO, formaPagamento: TipoPagamento, total: Double, idUsuario: Int) : this(
        null,
        LocalDate.now(),
        dto.total,
        TipoPagamento.getTipoPagamento(dto.formaPagamento),
        null,
        null,
        null
    )
}