package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.CaracteristicaDetalhe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Component

@Repository
interface CaracteristicaDetalheJpaRepository : JpaRepository<CaracteristicaDetalhe, Int>

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

    // Adicionar este método à classe CaracteristicaDetalheRepository
    
/*    @Transactional
    @Modifying
    @Query("UPDATE CaracteristicaDetalhe cd SET cd.imagem = :imagem WHERE cd.idCaracteristicaDetalhe = :id")
    fun updateImagem(id: Int, imagem: ByteArray): Int*/
}