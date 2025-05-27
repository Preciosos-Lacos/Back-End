package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.dto.pedido.CadastroItemPedidoDTO
import com.lacos_preciosos.preciososLacos.dto.DadosDetalhePedido
import com.lacos_preciosos.preciososLacos.service.PedidoService
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

//@RestController
//@RequestMapping("/pedidos")
class FavoritoController(private val pedidoService: PedidoService) {
//
//    @PostMapping
//    @Tag(name = "Cadastro de Pedido")
//    fun createPedido(@RequestBody @Valid criacaoPedidoDTO: CadastroItemPedidoDTO): ResponseEntity<DadosDetalhePedido> {
//
//        try {
//            var pedido = pedidoService.createPedido(criacaoPedidoDTO)
//            return ResponseEntity.status(201).body(pedido)
//        } catch (e: RuntimeException){
//            return ResponseEntity.status(404).build()
//        }
//    }
//
//    @GetMapping
//    @Tag(name = "Listagem de Pedido")
//    fun getAllPedidos(): ResponseEntity<List<DadosDetalhePedido>> {
//
//        try {
//            var pedidos = pedidoService.getAllPedidos()
//            return ResponseEntity.status(200).body(pedidos)
//        } catch (e: RuntimeException){
//            return ResponseEntity.status(204).build()
//        }
//    }
//
//    @GetMapping("/{id}")
//    fun getOneModelo(@PathVariable id: Int): ResponseEntity<DadosDetalhePedido> {
//        try {
//            var pedido = pedidoService.getOnePedido(id)
//            return ResponseEntity.status(200).body(pedido)
//        } catch (e: RuntimeException){
//            return ResponseEntity.status(404).build()
//        }
//    }
//
//    @PutMapping("/{id}")
//    @Tag(name = "Atualização de Pedido")
//    fun updateModelo(@PathVariable id: Int, @RequestBody @Valid atualizacaoPedidoDTO: CadastroItemPedidoDTO):
//            ResponseEntity<DadosDetalhePedido> {
//
//        try {
//            return ResponseEntity.status(200).body(pedidoService.updatePedido(id, atualizacaoPedidoDTO))
//        } catch (ex: ValidacaoException) {
//            return ResponseEntity.status(404).build()
//        }
//    }
//
//
//    @DeleteMapping("/{id}")
//    @Tag(name = "Exclusão de Pedido")
//    fun deletePedido(@PathVariable id: Int): ResponseEntity<Void> {
//        try {
//            pedidoService.deletePedido(id)
//            return ResponseEntity.status(204).build()
//        } catch (ex: ValidacaoException) {
//            return ResponseEntity.status(404).build()
//        }
//    }
}