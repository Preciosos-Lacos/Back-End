package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.Favorito
import org.springframework.data.jpa.repository.JpaRepository

interface FavoritoRepository : JpaRepository<Favorito, Int> {
    fun findAllByUsuarioId(usuarioId: Int): List<Favorito>
    fun existsByUsuarioIdAndProdutoId(idUsuario: Int, idModelo: Int): Boolean
}
