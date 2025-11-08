package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.repository.PedidoRepository
import org.springframework.stereotype.Service
import java.util.Date

@Service
class DashboardService(val pedidoRepository: PedidoRepository) {

    fun listarPedidos(): List<Map<String, Any>> {

        val listaPedidos = pedidoRepository.findAllPedidos()
        if (listaPedidos.isEmpty()) throw RuntimeException("Nenhum pedido encontrado")
        return listaPedidos
    }

    fun resumoGeral(): Map<String, Any> {
        val pedidos24hHoje = pedidoRepository.countPedidosUltimas24h()
        val pedidos24hOntem = pedidoRepository.countPedidosOntem()
        val entregasProgramadas = pedidoRepository.countEntregasProgramadas()
        val entregasAtrasadas = pedidoRepository.countEntregasAtrasadas()
        val atrasoMedioDias = pedidoRepository.atrasoMedioDias()
        val pedidosPendentes = pedidoRepository.countPedidosPendentes()
        val vendasDia = pedidoRepository.totalVendasDia()
        val vendasOntem = pedidoRepository.totalVendasOntem()
        val ticketMedio = pedidoRepository.ticketMedio()
        val ticketSemana = pedidoRepository.ticketMedioSemanaPassada()

        fun variacaoPercentual(atual: Double, anterior: Double): Double {
            return if (anterior == 0.0) 100.0 else ((atual - anterior) / anterior) * 100
        }

        return mapOf(
            "pedidos24h" to pedidos24hHoje,
            "variacaoPedidos" to variacaoPercentual(pedidos24hHoje.toDouble(), pedidos24hOntem.toDouble()),
            "entregasProgramadas" to entregasProgramadas,
            "entregasAtrasadas" to entregasAtrasadas,
            "atrasoMedioDias" to atrasoMedioDias,
            "pedidosPendentes" to pedidosPendentes,
            "vendasDia" to vendasDia,
            "variacaoVendas" to variacaoPercentual(vendasDia, vendasOntem),
            "ticketMedio" to ticketMedio,
            "ticketSemana" to ticketSemana,
            "variacaoTicket" to variacaoPercentual(ticketMedio, ticketSemana)
        )
    }

    fun listarEntregasDoDia(): List<Map<String, Any>> {

        val listaPedidos = pedidoRepository.listarEntregasDoDia()
        if (listaPedidos.isEmpty()) throw RuntimeException("Nenhuma Entrega Encontrada")
        return listaPedidos
    }

    fun listarVendasUltimos7Dias(): List<Map<String, Any>> {
        val vendas = pedidoRepository.listarVendasUltimos7Dias()
        if (vendas.isEmpty()) throw RuntimeException("Nenhuma venda encontrada nos últimos 7 dias")
        return vendas
    }

}