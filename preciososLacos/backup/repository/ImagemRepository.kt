package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.Imagem
import org.springframework.data.jpa.repository.JpaRepository

interface ImagemRepository : JpaRepository<Imagem, Long> {
    fun findAllByProdutoIdProduto(idProduto: Int): List<Imagem>
}
