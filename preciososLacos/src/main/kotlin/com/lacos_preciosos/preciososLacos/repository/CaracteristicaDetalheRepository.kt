package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.dto.cor.UpdateCorDTO
import com.lacos_preciosos.preciososLacos.model.CaracteristicaDetalhe
import com.lacos_preciosos.preciososLacos.model.Usuario
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository

@Repository
interface CaracteristicaDetalheRepository: JpaRepository<CaracteristicaDetalhe, Int> {

    @Transactional
    @Modifying
    @Query("INSERT INTO caracteristica_detalhe (descricao, hexa_decimal, preco, caracteristica_id_caracteristica) VALUES (:nomeDaCor, :hexaDecimal, :preco, 1)",
        nativeQuery = true)
    fun saveCor(nomeDaCor: String, hexaDecimal: String, preco: Double)

    @Query("SELECT * FROM caracteristica_detalhe c WHERE c.id_caracteristica_detalhe = :id", nativeQuery = true)
    fun findCorById(id: Int): CaracteristicaDetalhe

    @Transactional
    @Modifying
    @Query("UPDATE caracteristica_detalhe c SET c.preco = :preco WHERE c.id_caracteristica_detalhe = :id", nativeQuery = true)
    fun updateCor(id: Int, preco: Double): Int


    @Transactional
    @Modifying
    @Query("UPDATE CaracteristicaDetalhe c SET c.imagem = :imagem WHERE c.idCaracteristicaDetalhe = :id")
    fun updateImagem(id: Int?, imagem: ByteArray): Int

}