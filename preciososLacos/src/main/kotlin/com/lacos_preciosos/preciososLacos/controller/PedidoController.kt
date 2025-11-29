package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.dto.pedido.PedidoDTO
import com.lacos_preciosos.preciososLacos.dto.pedido.StatusPagamentoDTO
import com.lacos_preciosos.preciososLacos.service.PedidoService
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/pedidos")
class PedidoController(private val pedidoService: PedidoService) {
    @GetMapping("/ultimo/{idUsuario}")
    fun getUltimoPedidoUsuario(@PathVariable idUsuario: Int): ResponseEntity<PedidoDTO> {
        val pedido = pedidoService.getUltimoPedidoUsuario(idUsuario)
        return if (pedido != null) ResponseEntity.ok(pedido) else ResponseEntity.notFound().build()
    }

    @PostMapping
    @Tag(name = "Cadastro de Pedido")
    fun criarPedido(@RequestBody pedidoDTO: PedidoDTO): ResponseEntity<PedidoDTO> {
        return try {
            val pedido = pedidoService.criarPedido(pedidoDTO)
            ResponseEntity.status(201).body(pedido)
        } catch (e: RuntimeException) {
            ResponseEntity.status(404).build()
        }
    }


    @PutMapping("/{id}/status")
    fun atualizarStatusPedido(@PathVariable id: Int, @RequestBody body: Map<String, String>): ResponseEntity<PedidoDTO> {
        val novoStatus = body["statusPedido"] ?: return ResponseEntity.badRequest().build()
        return try {
            val pedidoAtualizado = pedidoService.atualizarStatusPedido(id, novoStatus)
            ResponseEntity.ok(pedidoAtualizado)
        } catch (e: Exception) {
            ResponseEntity.status(404).build()
        }
    }

    @PutMapping("/{id}/pagamento")
    fun atualizarStatusPagamento(@PathVariable id: Int, @RequestBody body: StatusPagamentoDTO): ResponseEntity<Any> {
        return try {
            val pedidoAtualizado = pedidoService.atualizarStatusPagamento(id, body.statusPagamento)
            ResponseEntity.ok(pedidoAtualizado)
        } catch (e: RuntimeException) {
            ResponseEntity.status(404).body(mapOf("erro" to e.message))
        } catch (e: Exception) {
            ResponseEntity.status(500).body(mapOf("erro" to "Erro interno inesperado"))
        }
    }
    
    @GetMapping
    @Tag(name = "Listagem e Busca de Pedido")
    fun listarOuBuscarPedidos(
        @RequestParam(required = false) dataInicio: String?,
        @RequestParam(required = false) dataFim: String?,
        @RequestParam(required = false) statusPagamento: String?,
        @RequestParam(required = false) statusPedido: String?
    ): ResponseEntity<List<PedidoDTO>> {
        val pedidos = pedidoService.buscarPedidos(
            null,
            dataInicio,
            dataFim,
            statusPagamento,
            statusPedido
        )
        return ResponseEntity.ok(pedidos)
    }

    @GetMapping("/{id}")
    fun detalharPedido(@PathVariable id: Int): ResponseEntity<PedidoDTO> {
        val pedido = pedidoService.detalharPedido(id)
        return if (pedido != null) {
            ResponseEntity.ok(pedido)
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @GetMapping("/resumo/{idPedido}")
    @Tag(name = "Buscar resumo de Pedido")
    fun buscaResumoPedido(@PathVariable idPedido: Int): ResponseEntity<Any> {
        return try {
            val resumo = pedidoService.buscaResumoPedido(idPedido)
            if (resumo != null)
                ResponseEntity.ok(resumo)
            else
                ResponseEntity.noContent().build()
        } catch (e: Exception) {
            ResponseEntity.status(500).body("Erro ao buscar resumo do pedido: ${e.message}")
        }
    }

    @GetMapping("/resumo-completo/{idPedido}")
    @Tag(name = "Buscar resumo completo de Pedido")
    fun buscaResumoCompletoPedido(@PathVariable idPedido: Int): ResponseEntity<Any> {
        return try {
            val resumo = pedidoService.buscaResumoCompletoPedido(idPedido)
            if (resumo != null)
                ResponseEntity.ok(resumo)
            else
                ResponseEntity.noContent().build()
        } catch (e: Exception) {
            ResponseEntity.status(500).body("Erro ao buscar resumo completo do pedido: ${e.message}")
        }
    }

    @PutMapping("/{id}")
    @Tag(name = "Atualização de Pedido")
    fun atualizarPedido(@PathVariable id: Int, @RequestBody pedidoDTO: PedidoDTO): ResponseEntity<PedidoDTO> {
        return try {
            ResponseEntity.ok(pedidoService.atualizarPedido(id, pedidoDTO))
        } catch (ex: ValidacaoException) {
            ResponseEntity.status(404).build()
        }
    }


    @DeleteMapping("/{id}")
    @Tag(name = "Exclusão de Pedido")
    fun excluirPedido(@PathVariable id: Int): ResponseEntity<Void> {
        return try {
            pedidoService.excluirPedido(id)
            ResponseEntity.noContent().build()
        } catch (ex: ValidacaoException) {
            ResponseEntity.status(404).build()
        }
    }

    // Carrinho: adiciona produto ao carrinho existente ou cria novo pedido carrinho=true
    @PostMapping("/carrinho")
    fun adicionarAoCarrinho(@RequestBody body: Map<String, Int>): ResponseEntity<PedidoDTO> {
        val idUsuario = body["idUsuario"] ?: return ResponseEntity.badRequest().build()
        val idProduto = body["idProduto"] ?: return ResponseEntity.badRequest().build()
        return try {
            val dto = pedidoService.adicionarProdutoAoCarrinho(idUsuario, idProduto)
            ResponseEntity.status(201).body(dto)
        } catch (e: RuntimeException) {
            ResponseEntity.status(404).build()
        }
    }

    @GetMapping("/carrinho/{idUsuario}")
    fun obterCarrinho(@PathVariable idUsuario: Int): ResponseEntity<PedidoDTO> {
        val dto = pedidoService.obterCarrinhoDoUsuario(idUsuario) ?: return ResponseEntity.noContent().build()
        return ResponseEntity.ok(dto)
    }

    @DeleteMapping("/carrinho/{idProduto}/usuario/{idUsuario}")
    fun removerDoCarrinho(@PathVariable idUsuario: Int, @PathVariable idProduto: Int): ResponseEntity<PedidoDTO> {
        val dto = pedidoService.removerProdutoDoCarrinho(idUsuario, idProduto) ?: return ResponseEntity.noContent().build()
        return ResponseEntity.ok(dto)
    }

    @GetMapping("/carrinho/{idUsuario}/produtos")
    fun listarProdutosCarrinho(@PathVariable idUsuario: Int): ResponseEntity<List<com.lacos_preciosos.preciososLacos.dto.produto.ProdutoDTO>> {
        val lista = pedidoService.listarProdutosCarrinho(idUsuario)
        return if (lista.isEmpty()) ResponseEntity.noContent().build() else ResponseEntity.ok(lista)
    }

    @GetMapping("/meus")
    fun listarMeusPedidos(): ResponseEntity<Any> {
        return try {
            val lista = pedidoService.listarMeusPedidos()
            if (lista.isEmpty()) ResponseEntity.noContent().build() else ResponseEntity.ok(lista)
        } catch (e: RuntimeException) {
            ResponseEntity.status(401).body(mapOf("erro" to (e.message ?: "Não autenticado")))
        }
    }
}