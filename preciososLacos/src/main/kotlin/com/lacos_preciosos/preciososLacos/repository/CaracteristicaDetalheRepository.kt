package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.CaracteristicaDetalhe
import com.lacos_preciosos.preciososLacos.model.Modelo
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CaracteristicaDetalheRepository : JpaRepository<CaracteristicaDetalhe, Int> {

    @Transactional
    @Modifying
    @Query(
        "INSERT INTO caracteristica_detalhe (descricao, hexa_decimal, preco, caracteristica_id_caracteristica) VALUES (:nomeDaCor, :hexaDecimal, :preco, 1)",
        nativeQuery = true
    )
    fun saveCor(nomeDaCor: String?, hexaDecimal: String?, preco: Double)

    @Query("SELECT * FROM caracteristica_detalhe c WHERE c.id_caracteristica_detalhe = :id", nativeQuery = true)
    fun findCorById(id: Int): CaracteristicaDetalhe

    @Query("SELECT * FROM caracteristica_detalhe WHERE caracteristica_id_caracteristica = 1", nativeQuery = true)
    fun findAllCores(): List<CaracteristicaDetalhe>

    @Query(
        value = """
        SELECT mo.* 
        FROM modelo mo
        INNER JOIN modelo_caracteristica_detalhe mcd 
            ON mo.id_modelo = mcd.modelo_id_modelo
        WHERE mcd.caracteristica_id_caracteristica_detalhe = :id
    """,
        nativeQuery = true
    )
    fun findAllModeloByCor(id: Int?): List<Modelo>

    @Transactional
    @Modifying
    @Query(
        "UPDATE caracteristica_detalhe c SET c.preco = :preco WHERE c.id_caracteristica_detalhe = :id",
        nativeQuery = true
    )
    fun updateCor(id: Int, preco: Double): Int


    @Transactional
    @Modifying
    @Query("UPDATE CaracteristicaDetalhe c SET c.imagem = :imagem WHERE c.idCaracteristicaDetalhe = :id")
    fun updateImagem(id: Int?, imagem: ByteArray): Int



    @Transactional
    @Modifying
    @Query(
        value = """
            INSERT INTO modelo_caracteristica_detalhe 
            (modelo_id_modelo, caracteristica_id_caracteristica_detalhe)
            VALUES (:idModelo, :idCor)
        """,
        nativeQuery = true
    )
    fun insertModeloCor(idModelo: Int, idCor: Int?)



    @Query(
        value = "SELECT * FROM modelo_caracteristica_detalhe WHERE caracteristica_id_caracteristica_detalhe = :idCor",
        nativeQuery = true
    )
    fun findByCaracteristicaId(idCor: Int): List<CaracteristicaDetalhe>

    @Query(
        value = "DELETE FROM modelo_caracteristica_detalhe WHERE caracteristica_id_caracteristica_detalhe = :idCor",
        nativeQuery = true
    )
    fun deleteByCaracteristicaId(idCor: Int)

}