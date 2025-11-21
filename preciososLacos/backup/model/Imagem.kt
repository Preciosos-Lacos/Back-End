package com.lacos_preciosos.preciososLacos.model

import jakarta.persistence.*

@Entity
data class Imagem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val urlImagem: String? = null,
    @ManyToOne
    val produto: Produto? = null
)

annotation class Produto
