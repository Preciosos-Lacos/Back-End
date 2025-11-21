package com.lacos_preciosos.preciososLacos.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.Date

@Entity
@Table(name = "banners")
data class Banner(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var titulo: String? = null,

    @Column(name = "imagem_url", nullable = false)
    var imagemUrl: String,

    var linkDestino: String? = null,

    var ordem: Int = 0,

    var ativo: Boolean = true,

    var dataInicio: Date? = null,

    var dataFim: Date? = null,

    var createdAt: LocalDateTime = LocalDateTime.now(),

    var updatedAt: LocalDateTime = LocalDateTime.now()
){
    constructor() : this(null, null, "", null, 0, true, null, null, java.time.LocalDateTime.now(), java.time.LocalDateTime.now())
}
