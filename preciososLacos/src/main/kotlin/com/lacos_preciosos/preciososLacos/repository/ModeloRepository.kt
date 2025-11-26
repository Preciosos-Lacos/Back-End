
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

    // Buscar os modelos mais vendidos (top 6)
    @Query("""
        SELECT m.* FROM modelo m
        JOIN produto p ON m.id_modelo = p.modelo_id_modelo
        JOIN pedido_produto pp ON p.id_produto = pp.id_produto
        GROUP BY m.id_modelo
        ORDER BY COUNT(pp.id_produto) DESC
        LIMIT 6
    """, nativeQuery = true)
    fun findMaisVendidos(): List<Modelo>

    // Buscar 6 modelos aleatórios
    @Query("""
        SELECT * FROM modelo
        ORDER BY RAND()
        LIMIT 6
    """, nativeQuery = true)
    fun findAleatorios(): List<Modelo>

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

    // Adicionar este método ao ModeloRepository
    
    @Transactional
    @Modifying
    @Query("UPDATE Modelo m SET m.foto = :foto WHERE m.idModelo = :idModelo")
    fun updateFoto(idModelo: Int?, foto: ByteArray): Int

    @Query(
        value = "SELECT m.* FROM modelo m JOIN favorito_modelo f ON m.id_modelo = f.id_modelo WHERE f.id_usuario = :idUsuario",
        nativeQuery = true
    )
    fun findFavoritosByUsuario(idUsuario: Int): List<Modelo>
}