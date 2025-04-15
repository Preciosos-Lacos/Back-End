package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.Modelo
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ModeloRepository : JpaRepository<Modelo, Int> {

    fun findByNomeModeloContainingIgnoreCase(nomeModelo: String): Optional<Modelo>

    @Query("UPDATE Modelo m SET m.foto = :foto WHERE m.idModelo = :idModelo")
    @Transactional
    @Modifying
    fun updateFoto(idModelo: Int?, foto: String): Int
}