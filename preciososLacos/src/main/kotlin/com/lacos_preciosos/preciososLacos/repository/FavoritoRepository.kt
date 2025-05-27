package com.lacos_preciosos.preciososLacos.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import jakarta.transaction.Transactional

interface FavoritoRepository : JpaRepository<Favorito, Int> {

    @Transactional
    @Modifying
    @Query("update Favorito f set f.marcado = ?3 where f.usuarioId = ?1 and f.produtoId = ?2")
    fun atualizarMarcado(usuarioId: Int, produtoId: Int, marcado: String): Int
}