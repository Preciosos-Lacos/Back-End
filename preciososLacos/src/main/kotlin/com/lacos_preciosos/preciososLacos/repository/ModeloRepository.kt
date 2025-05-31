package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.Modelo
import com.lacos_preciosos.preciososLacos.model.Usuario
import jakarta.persistence.Entity
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ModeloRepository : JpaRepository<Modelo, Int> {

    fun findByNomeModeloContainingIgnoreCase(nomeModelo: String): Optional<Modelo>

    @Transactional
    @Modifying
    @Query(
        value = "INSERT INTO favorito_modelo (id_modelo, id_usuario) VALUES (:idModelo, :idUsuario)",
        nativeQuery = true
    )
    fun adicionarFavorito(idModelo: Int,idUsuario: Int)

    @Transactional
    @Modifying
    @Query(
        value = "DELETE FROM favorito_modelo WHERE id_modelo = :idModelo AND id_usuario = :idUsuario",
        nativeQuery = true
    )
    fun deleteFavorito(idModelo: Int, idUsuario: Int)

//    @Query("UPDATE Modelo m SET m.foto = :foto WHERE m.idModelo = :idModelo")
//    @Transactional
//    @Modifying
//    fun updateFoto(idModelo: Int?, foto: ByteArray): Int
}