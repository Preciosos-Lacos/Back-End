package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.model.Produto
import com.lacos_preciosos.preciososLacos.service.ProdutoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/produtos")
class ProdutoController(val produtoService: ProdutoService) {

    @GetMapping
    fun getAllProdutos(): ResponseEntity<List<Produto>> {
        return ResponseEntity.ok(produtoService.getAllProdutos())
    }

}