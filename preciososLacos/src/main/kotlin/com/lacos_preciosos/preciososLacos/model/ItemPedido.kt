package com.lacos_preciosos.preciososLacos.model

import com.lacos_preciosos.preciososLacos.dto.pedido.CadastroItemPedidoDTO
import jakarta.persistence.*

@Entity
data class ItemPedido(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idItemPedido: Int? = null,

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    var pedido: Pedido? = null,

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    var produto: Produto? = null,

    var quantidade: Int = 0,

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    var usuario: Usuario? = null,
) {
    constructor(dto: CadastroItemPedidoDTO, pedido: Pedido, produto: Produto, usuario: Usuario) : this(
        idItemPedido = null,
        pedido = pedido,
        produto = produto,
        quantidade = dto.quantidade,
        usuario = usuario
    )

}