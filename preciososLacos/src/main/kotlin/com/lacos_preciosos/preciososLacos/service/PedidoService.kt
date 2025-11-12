package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.PedidoResumoDTO
import com.lacos_preciosos.preciososLacos.dto.pedido.CadastroPedidoDTO
import com.lacos_preciosos.preciososLacos.dto.pedido.DadosDetalhePedido
import com.lacos_preciosos.preciososLacos.dto.pedido.PedidoResumoCompletoDTO
import com.lacos_preciosos.preciososLacos.dto.produto.ProdutoDTO
import com.lacos_preciosos.preciososLacos.model.Pedido
import com.lacos_preciosos.preciososLacos.model.Produto
import com.lacos_preciosos.preciososLacos.repository.*
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

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

        return DadosDetalhePedido(pedido.total, pedido.formaPagamento.toString())
    }

    fun getAllPedidos(): List<DadosDetalhePedido> {
        var listaPedidos = pedidoRepository.findAll();

        if (listaPedidos.isEmpty()) {
            throw RuntimeException("Nenhum pedido encontrado")
        }

        return listaPedidos.stream().map { p -> DadosDetalhePedido(p.total, p.formaPagamento.toString()) }.toList()
    }

    fun getOnePedido(id: Int): DadosDetalhePedido {
        var pedido = pedidoRepository.findById(id);

        if (pedido.isEmpty) {
            throw RuntimeException("Pedido com esse ID não encontrado")
        }

        return DadosDetalhePedido(pedido.get().total, pedido.get().formaPagamento.toString())
    }

    fun buscaResumoPedido(idPedido: Int): PedidoResumoDTO? {
        val resultado = pedidoRepository.buscaResumoPedido(idPedido)

        if (resultado.isEmpty()) return null

        val r = resultado.first()

        return PedidoResumoDTO(
            id = (r["id"] as Number).toInt(),
            total = r["total"]?.toString() ?: "R$0,00",
            formaPagamento = r["formaPagamento"]?.toString() ?: "Desconhecida",
            formaEnvio = r["formaEnvio"]?.toString() ?: "Vendedor",
            cepEntrega = r["cepEntrega"]?.toString() ?: "00000000"
        )
    }

    fun updatePedido(id: Int, dto: CadastroPedidoDTO): DadosDetalhePedido {
        var pedido = pedidoRepository.findById(id);

        if (pedido.isEmpty) {
            throw RuntimeException("Pedido com esse ID não encontrado")
        }

        val pedidoAtualizado = Pedido(dto)
        pedidoAtualizado.idPedido = id

        var pedido1 = pedidoRepository.save(pedidoAtualizado)
        return DadosDetalhePedido(pedido1.total, pedido1.formaPagamento.toString())
    }

    fun deletePedido(id: Int) {
        var existe = pedidoRepository.existsById(id)

        if (existe) {
            pedidoRepository.deleteById(id)
        } else {
            throw ValidacaoException("Pedido não encontrado")
        }
    }

    @Transactional(readOnly = true)
    fun buscaResumoCompletoPedido(idPedido: Int): PedidoResumoCompletoDTO? {
        val pedido = pedidoRepository.findById(idPedido).orElse(null) ?: return null

        val produtosDTO: List<ProdutoDTO> = pedido.produtos?.map { produto ->
            ProdutoDTO(
                idProduto = produto.idProduto,
                nome = produto.nome,
                colecao = produto.modelo?.nomeModelo,
                tamanho = produto.tamanho,
                tipoLaco = produto.tipoLaco,
                foto = produto.modelo?.getFotoBase64(),
                preco = produto.preco
            )
        } ?: emptyList()

        return PedidoResumoCompletoDTO(
            idPedido = pedido.idPedido?.toLong() ?: 0L,
            nomeCliente = pedido.usuario?.nomeCompleto,
            telefone = pedido.usuario?.telefone,
            dataPedido = pedido.dataPedido,
            previsaoEntrega = null,
            total = BigDecimal.valueOf(pedido.total),
            formaPagamento = pedido.formaPagamento,
            statusPagamento = pedido.statusPagamento?.status,
            statusPedido = pedido.statusPedido?.status,
            produtos = produtosDTO
        )
    }

}