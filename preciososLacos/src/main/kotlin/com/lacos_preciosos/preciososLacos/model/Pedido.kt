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

    var dataPedido: LocalDate = LocalDate.now(),

    var total: Double = 0.0,

    var formaPagamento: TipoPagamento? = null,

    @ManyToOne
    var usuario: Usuario? = null,

    @ManyToOne
    var statusPedido: StatusPedido? = null,

    @ManyToOne
    var statusPagamento: StatusPagamento? = null,

    @ManyToMany
    @JoinTable(
        name = "pedido_produto",
        joinColumns = [JoinColumn(name = "idPedido")],
        inverseJoinColumns = [JoinColumn(name = "idProduto")]
    )

    var produtos: List<Produto>? = null,

    var carrinho: Boolean = true
) {
    constructor(dto: CadastroPedidoDTO) : this(
        null,
        LocalDate.now(),
        dto.total,
        TipoPagamento.getTipoPagamento(dto.formaPagamento),
        null,
        null,
        null,
        null,
        dto.carrinho
    )


}