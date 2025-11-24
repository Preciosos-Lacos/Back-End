package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.dto.pedido.CheckoutResumoDTO
import com.lacos_preciosos.preciososLacos.dto.pedido.FinalizarPedidoRequest
import com.lacos_preciosos.preciososLacos.service.CheckoutService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/checkout")
class CheckoutController(
    private val checkoutService: CheckoutService
) {

    @GetMapping("/{idUsuario}")
    @Tag(name = "Checkout - Resumo do carrinho")
    fun obterCheckout(@PathVariable idUsuario: Int): ResponseEntity<Any> {
        return try {
            val resumo: CheckoutResumoDTO = checkoutService.obterCheckout(idUsuario)
            ResponseEntity.ok(resumo)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("erro" to (e.message ?: "Requisição inválida")))
        } catch (e: Exception) {
            ResponseEntity.status(500).body(mapOf("erro" to "Erro interno ao obter checkout"))
        }
    }

    @PostMapping("/finalizar")
    @Tag(name = "Checkout - Finalizar pedido")
    fun finalizarPedido(@RequestBody request: FinalizarPedidoRequest): ResponseEntity<Any> {
        return try {
            val resumo = checkoutService.finalizarPedido(request)
            ResponseEntity.ok(resumo)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("erro" to (e.message ?: "Requisição inválida")))
        } catch (e: Exception) {
            ResponseEntity.status(500).body(mapOf("erro" to "Erro interno ao finalizar pedido"))
        }
    }
}

