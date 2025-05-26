package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.pedido.CadastroPedidoDTO
import com.lacos_preciosos.preciososLacos.dto.pedido.DadosDetalhePedido
import com.lacos_preciosos.preciososLacos.model.Pedido
import com.lacos_preciosos.preciososLacos.repository.PedidoRepository
import com.lacos_preciosos.preciososLacos.repository.UsuarioRepository
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.springframework.stereotype.Service
import java.lang.RuntimeException

@Service
class PedidoService(val pedidoRepository: PedidoRepository, val usuarioRepository: UsuarioRepository) {

    fun createPedido(cadastroPedidoDTO: CadastroPedidoDTO): DadosDetalhePedido {

      val usuario = usuarioRepository.findById(cadastroPedidoDTO.idUsuario)

        if (usuario.isEmpty) {
            throw RuntimeException("Usuário com esse ID não foi encontrado")
        }

        val pedido = Pedido(cadastroPedidoDTO)
        pedido.usuario = usuario.get()
        return DadosDetalhePedido(pedido)
    }

    fun getAllPedidos(): List<DadosDetalhePedido> {
        var listaPedidos = pedidoRepository.findAll();

        if(listaPedidos.isEmpty()) {
            throw RuntimeException("Nenhum pedido encontrado")
        }

        return listaPedidos.stream().map { p -> DadosDetalhePedido(p) }.toList()
    }

    fun getOnePedido(id: Int): DadosDetalhePedido {
        var pedido = pedidoRepository.findById(id);

        if(pedido.isEmpty) {
            throw RuntimeException("Pedido com esse ID não encontrado")
        }

        return DadosDetalhePedido(pedido.get())
    }

    fun updatePedido(id: Int, dto: CadastroPedidoDTO): DadosDetalhePedido {
        var pedido = pedidoRepository.findById(id);

        if(pedido.isEmpty) {
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