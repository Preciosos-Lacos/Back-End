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
        value = "INSERT INTO caracteristica_detalhe (descricao, hexa_decimal, preco, caracteristica_id_caracteristica) VALUES (:nomeDaCor, :hexaDecimal, :preco, 1)",
        nativeQuery = true
    )
    fun saveCor(nomeDaCor: String?, hexaDecimal: String?, preco: Double)

    @Transactional
    @Modifying
    @Query(
        "INSERT INTO caracteristica_detalhe (descricao, preco, imagem, ativo, caracteristica_id_caracteristica) VALUES (:nome, :preco, :foto, true, 3)",
        nativeQuery = true
    )
    fun saveTipoLaco(nome: String?, preco: Double, foto: ByteArray)

    @Query(
        value = """
        SELECT 
            cd.id_caracteristica_detalhe as id,
            cd.descricao,
            cd.preco,
            cd.imagem,
            GROUP_CONCAT(m.nome_modelo) AS modelos
        FROM caracteristica_detalhe cd
        LEFT JOIN modelo_caracteristica_detalhe mcd 
               ON mcd.caracteristica_id_caracteristica_detalhe = cd.id_caracteristica_detalhe
        LEFT JOIN modelo m 
               ON m.id_modelo = mcd.modelo_id_modelo
        WHERE cd.caracteristica_id_caracteristica = 3
          AND cd.ativo = TRUE
        GROUP BY cd.id_caracteristica_detalhe;
    """,
        nativeQuery = true
    )
    fun getAllTipoLaco(): List<Map<String, Any>>

    @Transactional
    @Modifying
    @Query("""
        UPDATE caracteristica_detalhe SET descricao = :nome, preco = :preco,
        imagem = CASE 
                    WHEN :foto IS NOT NULL THEN :foto 
                    ELSE imagem 
                 END
    WHERE id_caracteristica_detalhe = :id""", nativeQuery = true)
    fun updateTipoLaco(nome: String?, preco: Double, foto: ByteArray?, id: Int): Int

    @Transactional
    @Modifying
    @Query(
        "UPDATE caracteristica_detalhe SET ativo = FALSE WHERE id_caracteristica_detalhe = :id;",
        nativeQuery = true
    )
    fun deleteTipoLaco(id: Int)


    @Query(
        value = "SELECT * FROM caracteristica_detalhe WHERE descricao = :nomeDaCor AND hexa_decimal = :hexaDecimal LIMIT 1",
        nativeQuery = true
    )
    fun findByNomeAndHexa(nomeDaCor: String, hexaDecimal: String): CaracteristicaDetalhe?

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
        value = "SELECT modelo_id_modelo FROM modelo_caracteristica_detalhe WHERE caracteristica_id_caracteristica_detalhe = :idCor",
        nativeQuery = true
    )
    fun findModeloIdsByCaracteristicaId(idCor: Int): List<Int>

    @Transactional
    @Modifying
    @Query(
        value = "INSERT INTO modelo_caracteristica_detalhe (modelo_id_modelo, caracteristica_id_caracteristica_detalhe) SELECT :idModelo, :idCor WHERE NOT EXISTS (SELECT 1 FROM modelo_caracteristica_detalhe WHERE modelo_id_modelo = :idModelo AND caracteristica_id_caracteristica_detalhe = :idCor)",
        nativeQuery = true
    )
    fun insertModeloCorIfNotExists(idModelo: Int, idCor: Int?)

    @Query(
        value = "DELETE FROM modelo_caracteristica_detalhe WHERE caracteristica_id_caracteristica_detalhe = :idCor",
        nativeQuery = true
    )
    fun deleteByCaracteristicaId(idCor: Int)

    @Query(
        value = "SELECT cd.* FROM caracteristica_detalhe cd JOIN modelo_caracteristica_detalhe mcd ON cd.id_caracteristica_detalhe = mcd.caracteristica_id_caracteristica_detalhe WHERE mcd.modelo_id_modelo = :idModelo",
        nativeQuery = true
    )
    fun findAllByModelo(idModelo: Int): List<CaracteristicaDetalhe>

}