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

    fun listarEntregasDoDia(): List<Map<String, Any>> {

        val listaPedidos = pedidoRepository.listarEntregasDoDia()
        if (listaPedidos.isEmpty()) throw RuntimeException("Nenhuma Entrega Encontrada")
        return listaPedidos
    }
}