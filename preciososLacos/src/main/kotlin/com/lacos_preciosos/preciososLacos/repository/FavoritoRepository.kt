package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.Favorito
import com.lacos_preciosos.preciososLacos.model.FavoritoId
import org.springframework.data.jpa.repository.JpaRepository

interface FavoritoRepository : JpaRepository<Favorito, FavoritoId> {
    fun findAllByUsuarioId(usuarioId: Int): List<Favorito>
}
