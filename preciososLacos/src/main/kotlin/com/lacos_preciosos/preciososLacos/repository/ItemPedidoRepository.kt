package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.ItemPedido
import org.springframework.data.jpa.repository.JpaRepository

interface ItemPedidoRepository : JpaRepository<ItemPedido, Int>{
}