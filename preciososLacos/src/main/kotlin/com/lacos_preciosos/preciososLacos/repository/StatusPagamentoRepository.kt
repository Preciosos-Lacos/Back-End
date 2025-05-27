package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.StatusPagamento
import org.springframework.data.jpa.repository.JpaRepository

interface StatusPagamentoRepository: JpaRepository<StatusPagamento, Int> {
}