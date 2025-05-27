package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.pedido.CadastroItemPedidoDTO
import com.lacos_preciosos.preciososLacos.dto.DadosDetalhePedido
import com.lacos_preciosos.preciososLacos.model.Pedido
import com.lacos_preciosos.preciososLacos.repository.PedidoRepository
import com.lacos_preciosos.preciososLacos.repository.UsuarioRepository
import com.lacos_preciosos.preciososLacos.tipos.TipoPagamento
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ItemPedidoService(
    private val pedidoRepository: PedidoRepository,
    private val usuarioRepository: UsuarioRepository
) {
//    private val logger: Logger = LoggerFactory.getLogger(ItemPedidoService::class.java)
//
//    @Transactional
//    fun createPedido(cadastroItemPedidoDTO: CadastroItemPedidoDTO): DadosDetalhePedido {
//        logger.info("Iniciando criação de pedido para usuário com ID: ${cadastroItemPedidoDTO.idUsuario}")
//
//        val usuario = usuarioRepository.findById(cadastroItemPedidoDTO.idUsuario)
//            .orElseThrow {
//                logger.error("Usuário com ID ${cadastroItemPedidoDTO.idUsuario} não encontrado")
//                ValidacaoException("Usuário com ID ${cadastroItemPedidoDTO.idUsuario} não foi encontrado")
//            }
//
//        val tipoPagamento = TipoPagamento.getTipoPagamento(cadastroItemPedidoDTO.formaPagamento)
//            ?: throw ValidacaoException("Forma de pagamento inválida: ${cadastroItemPedidoDTO.formaPagamento}")
//
//        val pedido = Pedido(
//            idUsuario = cadastroItemPedidoDTO.idUsuario,
//            total = cadastroItemPedidoDTO.total as Double,
//            formaPagamento = tipoPagamento,
//            dto = TODO()
//        ).apply { this.usuario = usuario }
//
//        val savedPedido = pedidoRepository.save(pedido)
//        logger.info("Pedido criado com sucesso para o usuário ID: ${cadastroItemPedidoDTO.idUsuario}")
//
//        return DadosDetalhePedido(savedPedido)
//    }
//
//    fun getAllPedidos(): List<DadosDetalhePedido> {
//        logger.info("Buscando todos os pedidos cadastrados")
//
//        val pedidos = pedidoRepository.findAll()
//
//        if (pedidos.isEmpty()) {
//            logger.warn("Nenhum pedido encontrado na base de dados")
//            throw ValidacaoException("Nenhum pedido encontrado")
//        }
//
//        return pedidos.map { DadosDetalhePedido(it) }
//    }
//
//    fun getOnePedido(id: Int): DadosDetalhePedido {
//        logger.info("Buscando pedido com ID: $id")
//
//        val pedido = pedidoRepository.findById(id)
//            .orElseThrow {
//                logger.error("Pedido com ID $id não encontrado")
//                ValidacaoException("Pedido com ID $id não foi encontrado")
//            }
//
//        return DadosDetalhePedido(pedido)
//    }
//
//    @Transactional
//    fun updatePedido(id: Int, cadastroItemPedidoDTO: CadastroItemPedidoDTO): DadosDetalhePedido {
//        logger.info("Atualizando pedido com ID: $id")
//
//        val pedido = pedidoRepository.findById(id)
//            .orElseThrow {
//                logger.error("Pedido com ID $id não encontrado para atualização")
//                ValidacaoException("Pedido com ID $id não encontrado para atualização")
//            }
//
//        val tipoPagamento = TipoPagamento.getTipoPagamento(cadastroItemPedidoDTO.formaPagamento)
//            ?: throw ValidacaoException("Forma de pagamento inválida: ${cadastroItemPedidoDTO.formaPagamento}")
//
//        pedido.apply {
//            total = cadastroItemPedidoDTO.total
//            formaPagamento = tipoPagamento
//        }
//
//        val pedidoAtualizado = pedidoRepository.save(pedido)
//        logger.info("Pedido com ID $id atualizado com sucesso")
//        return DadosDetalhePedido(pedidoAtualizado)
//    }
//
//    @Transactional
//    fun deletePedido(id: Int) {
//        logger.info("Tentando excluir pedido com ID: $id")
//        if (!pedidoRepository.existsById(id)) {
//            logger.error("Pedido com ID $id não encontrado para exclusão")
//            throw ValidacaoException("Pedido com ID $id não foi encontrado")
//        }
//
//        pedidoRepository.deleteById(id)
//        logger.info("Pedido com ID $id excluído com sucesso")
//    }
}