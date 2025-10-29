package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.dto.pedido.ListaPedidosDTO
import com.lacos_preciosos.preciososLacos.service.DashboardService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dashboard")
class DashboardController(private val dashboardService: DashboardService) {

    @GetMapping
    fun listarPedidos(): ResponseEntity<List<Map<String, Any>>> {
        return try {
            ResponseEntity.ok(dashboardService.listarPedidos())
        } catch (ex: Exception) {
            ResponseEntity.notFound().build()
        }
    }

}