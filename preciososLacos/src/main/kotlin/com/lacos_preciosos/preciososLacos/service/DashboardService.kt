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
        val pedidosUltimas24h = pedidoRepository.countPedidosUltimas24h()
        val entregasProgramadas = pedidoRepository.countEntregasProgramadas()
        val entregasAtrasadas = pedidoRepository.countEntregasAtrasadas()
        val pedidosPendentes = pedidoRepository.countPedidosPendentes()
        val totalVendasDia = pedidoRepository.totalVendasDia()
        val ticketMedio = pedidoRepository.ticketMedio()

        return mapOf(
            "pedidos24h" to pedidosUltimas24h,
            "entregasProgramadas" to entregasProgramadas,
            "entregasAtrasadas" to entregasAtrasadas,
            "pedidosPendentes" to pedidosPendentes,
            "vendasDia" to totalVendasDia,
            "ticketMedio" to ticketMedio
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