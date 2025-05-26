package com.lacos_preciosos.preciososLacos.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class StatusPedido(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var statusPedido: Int? = null,

    var status: String
)
