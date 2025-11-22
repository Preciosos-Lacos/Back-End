package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.Banner
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BannerRepository : JpaRepository<Banner, Long> {
    fun findByAtivoTrueOrderByOrdemAsc(): List<Banner>
    fun findByAtivo(ativo: Boolean): List<Banner>
    @Query("SELECT b FROM Banner b WHERE (:ativo IS NULL OR b.ativo = :ativo) AND (:data IS NULL OR :data BETWEEN b.dataInicio AND b.dataFim) ORDER BY b.ordem ASC")
    fun findByFiltro(ativo: Boolean?, data: Date?): List<Banner>
}
