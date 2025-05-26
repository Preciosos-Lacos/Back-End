package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.model.Produto
import com.lacos_preciosos.preciososLacos.service.ProdutoService
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/produtos")
class ProdutoController(val produtoService: ProdutoService) {


// Criando um produto
    @PostMapping
    @Tag(name = "Cadastro de produto")
    fun cadastrarProduto(@RequestBody novoProduto:  CadastroProdutoDTO): ResponseEntity<CadastroProdutoDTO> {
        val produto = produtoService.save(novoProduto)
        return ResponseEntity.status(201).body(novoProduto)
    }

    //Listando todos os produtos do banco de dados
    @GetMapping
    @Tag(name = "Listagem de produto")
    fun listarProdutos(): ResponseEntity<List<Produto>> {

        try {
            return ResponseEntity.status(200).body(produtoService.listarProdutos())
        } catch (ex: ValidacaoException) {
            return ResponseEntity.status(204).build()
        }
    }

    // Atualizando produto
    @PutMapping("/{id}")
    @Tag(name = "Atualização de produto")
    fun updateProduto(
        @PathVariable id: Int,
        @RequestBody @Valid dto: CadastroProdutoDTO
    ): ResponseEntity<Produto> {
        return try {
            ResponseEntity.ok(produtoService.updateProduto(id, dto))
        } catch (ex: ValidacaoException) {
            ResponseEntity.notFound().build()
        }
    }

    // excluíndo um produto
    @DeleteMapping("/{id}")
    @Tag(name = "Exclusão de produto")
    fun deleteProduto(@PathVariable id: Int): ResponseEntity<Void> {
        try {
            produtoService.deleteModelo(id)
            return ResponseEntity.status(204).build()
        } catch (ex: ValidacaoException) {
            return ResponseEntity.status(404).build()
        }
    }

}