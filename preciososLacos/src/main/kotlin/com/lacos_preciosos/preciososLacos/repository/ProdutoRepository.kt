package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.Produto
import org.springframework.data.jpa.repository.JpaRepository

interface ProdutoRepository : JpaRepository<Produto, Int>{
}