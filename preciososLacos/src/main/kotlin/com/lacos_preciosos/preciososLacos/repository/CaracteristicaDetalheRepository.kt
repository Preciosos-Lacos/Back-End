package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.CaracteristicaDetalhe
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Component

@Repository
interface CaracteristicaDetalheJpaRepository : JpaRepository<CaracteristicaDetalhe, Int>{
    @Transactional
    @Modifying
    @Query("UPDATE CaracteristicaDetalhe c SET c.imagem = :imagem WHERE c.idCaracteristicaDetalhe = :id")
    fun updateImagem(id: Int?, imagem: ByteArray): Int
}


@Component
class CaracteristicaDetalheRepository(
    private val jpaRepository: CaracteristicaDetalheJpaRepository
) {
    fun create(caracteristicaDetalhe: CaracteristicaDetalhe): CaracteristicaDetalhe =
        jpaRepository.save(caracteristicaDetalhe)

    fun getAll(): List<CaracteristicaDetalhe> =
        jpaRepository.findAll()

    fun update(id: Int, updated: CaracteristicaDetalhe): CaracteristicaDetalhe {
        val existing = jpaRepository.findById(id).orElseThrow()
        val toSave = existing.copy(
            descricao = updated.descricao,
            caracteristica = updated.caracteristica
        )
        return jpaRepository.save(toSave)
    }

    fun delete(id: Int) {
        jpaRepository.deleteById(id)
    }

    fun subirImagem(id: Int, imagemBase64: ByteArray): CaracteristicaDetalhe {
        val detalhe = jpaRepository.findById(id).orElseThrow { RuntimeException("Característica detalhe não encontrada") }
        jpaRepository.updateImagem(id, imagemBase64)
        detalhe.imagem = imagemBase64 // Atualiza o objeto em memória
        return detalhe
    }
}