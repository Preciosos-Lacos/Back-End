package com.lacos_preciosos.preciososLacos.dto.banner

import com.lacos_preciosos.preciososLacos.model.Banner
import java.time.LocalDateTime
import java.util.Date

data class BannerDTO(
    val id: Long? = null,
    val titulo: String? = null,
    val imagemUrl: String,
    val linkDestino: String? = null,
    val ativo: Boolean = true,
    val dataInicio: Date? = null,
    val dataFim: Date? = null,
    val ordem: Int = 0, // ordem só para saída
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    constructor(banner: Banner) : this(
        id = banner.id,
        titulo = banner.titulo,
        imagemUrl = banner.imagemUrl,
        linkDestino = banner.linkDestino,
        ativo = banner.ativo,
        dataInicio = banner.dataInicio,
        dataFim = banner.dataFim,
        ordem = banner.ordem,
        createdAt = banner.createdAt,
        updatedAt = banner.updatedAt
    )
}
