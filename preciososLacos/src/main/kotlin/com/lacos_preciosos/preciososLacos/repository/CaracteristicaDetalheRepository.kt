package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.CaracteristicaDetalhe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CaracteristicaDetalheRepository : JpaRepository<CaracteristicaDetalhe, Int>