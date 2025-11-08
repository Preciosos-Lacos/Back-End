package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.service.DashboardService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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

    @GetMapping("/resumo")
    fun resumoGeral(): ResponseEntity<Map<String, Any>> {
        return try {
            ResponseEntity.ok(dashboardService.resumoGeral())
        } catch (ex: Exception) {
            ResponseEntity.notFound().build()
        }
    }


    @GetMapping("/entregasDoDia")
    fun listarEntregasDoDia(): ResponseEntity<List<Map<String, Any>>> {
        return try {
            ResponseEntity.ok(dashboardService.listarEntregasDoDia())
        } catch (ex: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/vendas7dias")
    fun listarVendasUltimos7Dias(): ResponseEntity<List<Map<String, Any>>> {
        return try {
            ResponseEntity.ok(dashboardService.listarVendasUltimos7Dias())
        } catch (ex: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/filtrado")
    fun listarDashboardFiltrada(
        @RequestParam(required = false) dataInicio: String?, @RequestParam(required = false) dataFim: String?,
        @RequestParam(required = false) statusPagamento: String?, @RequestParam(required = false) statusPedido: String?
    ): ResponseEntity<Map<String, Any>> {
        return try {
            ResponseEntity.ok(dashboardService.listarDashboardFiltrada(dataInicio, dataFim,
                    statusPagamento, statusPedido))
        } catch (ex: Exception) {
            ResponseEntity.notFound().build()
        }
    }


}