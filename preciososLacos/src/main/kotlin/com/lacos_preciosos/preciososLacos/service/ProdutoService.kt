package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.CadastroProdutoDTO
import com.lacos_preciosos.preciososLacos.model.Produto
import com.lacos_preciosos.preciososLacos.repository.ProdutoRepository
import org.springframework.stereotype.Service

@Service
class ProdutoService(val produtoRepository: ProdutoRepository) {

    fun getAllProdutos(): List<Produto> {
        return produtoRepository.findAll();
    }

}