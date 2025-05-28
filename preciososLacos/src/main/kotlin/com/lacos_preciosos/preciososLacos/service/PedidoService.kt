package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.DadosDetalhePedido
import com.lacos_preciosos.preciososLacos.dto.pedido.CadastroPedidoDTO
import com.lacos_preciosos.preciososLacos.model.Pedido
import com.lacos_preciosos.preciososLacos.model.Produto
import com.lacos_preciosos.preciososLacos.repository.*
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.springframework.stereotype.Service

@Service
class PedidoService(
    val pedidoRepository: PedidoRepository,
    val usuarioRepository: UsuarioRepository,
    val produtoRepository: ProdutoRepository,
    val statusPagamentoRepository: StatusPagamentoRepository,
    val statusPedidoRepository: StatusPedidoRepository
) {

    fun createPedido(cadastroPedidoDTO: CadastroPedidoDTO): DadosDetalhePedido {
        var usuarioOptional = usuarioRepository.findById(cadastroPedidoDTO.idUsuario)

        if (usuarioOptional.isEmpty) {
            throw RuntimeException("Usuário com esse ID não foi encontrado")
        }

        var produtos: List<Produto> = cadastroPedidoDTO.listaIdProdutos.map { id ->
            produtoRepository.findById(id)
                .orElseThrow { RuntimeException("Produto com ID $id não foi encontrado") }
        }

        val pedido = Pedido(cadastroPedidoDTO).apply {
            usuario = usuarioOptional.get()
            this.produtos = produtos
        }

        pedido.statusPedido = statusPedidoRepository.findById(1).get()
        pedido.statusPagamento = statusPagamentoRepository.findById(1).get()
        pedidoRepository.save(pedido)

        return DadosDetalhePedido(pedido)
    }

    fun getAllPedidos(): List<DadosDetalhePedido> {
        var listaPedidos = pedidoRepository.findAll();

        if (listaPedidos.isEmpty()) {
            throw RuntimeException("Nenhum pedido encontrado")
        }

        return listaPedidos.stream().map { p -> DadosDetalhePedido(p) }.toList()
    }

    fun getOnePedido(id: Int): DadosDetalhePedido {
        var pedido = pedidoRepository.findById(id);

        if (pedido.isEmpty) {
            throw RuntimeException("Pedido com esse ID não encontrado")
        }

        return DadosDetalhePedido(pedido.get())
    }

    fun updatePedido(id: Int, dto: CadastroPedidoDTO): DadosDetalhePedido {
        var pedido = pedidoRepository.findById(id);

        if (pedido.isEmpty) {
            throw RuntimeException("Pedido com esse ID não encontrado")
        }

        val pedidoAtualizado = Pedido(dto)
        pedidoAtualizado.idPedido = id

        return DadosDetalhePedido(pedidoRepository.save(pedidoAtualizado))
    }

    fun deletePedido(id: Int) {
        var existe = pedidoRepository.existsById(id)

        if (existe) {
            pedidoRepository.deleteById(id)
        } else {
            throw ValidacaoException("Pedido não encontrado")
        }
    }

}