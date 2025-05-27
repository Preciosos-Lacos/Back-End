package com.lacos_preciosos.preciososLacos.model

import com.lacos_preciosos.preciososLacos.dto.pedido.CadastroPedidoDTO
import com.lacos_preciosos.preciososLacos.tipos.TipoPagamento
import jakarta.persistence.*
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
    var statusPagamento: StatusPagamento?,

    @ManyToMany
    @JoinTable(
        name = "pedido_produto",
        joinColumns = [JoinColumn(name = "idPedido")],
        inverseJoinColumns = [JoinColumn(name = "idProduto")]
    )

    var produtos: List<Produto>? = null
) {
    constructor(dto: CadastroPedidoDTO) : this(
        null,
        LocalDate.now(),
        dto.total,
        TipoPagamento.getTipoPagamento(dto.formaPagamento),
        null,
        null,
        null,
        null
    )
}