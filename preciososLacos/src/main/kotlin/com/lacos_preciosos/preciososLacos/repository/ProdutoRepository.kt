package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.Modelo
import com.lacos_preciosos.preciososLacos.model.Produto
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ProdutoRepository : JpaRepository<Produto, Int>{

    @Transactional
    @Modifying
    @Query("update Produto p set p.tamanho = ?2, p.cor = ?3, p.tipoLaco = ?4, p.acabamento = ?5, p.preco = ?6 where p.idProduto = ?1")
    fun atualizarProduto(id: Int, tamanho: String, cor: String, tipoLaco: String, acabamento: String, preco: Double): Int
}