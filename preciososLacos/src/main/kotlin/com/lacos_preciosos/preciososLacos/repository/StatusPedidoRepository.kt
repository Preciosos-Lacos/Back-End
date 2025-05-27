package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.StatusPedido
import org.springframework.data.jpa.repository.JpaRepository

interface StatusPedidoRepository: JpaRepository<StatusPedido, Int> {
}