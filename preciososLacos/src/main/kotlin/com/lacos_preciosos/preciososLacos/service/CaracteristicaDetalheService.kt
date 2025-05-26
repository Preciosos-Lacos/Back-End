package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.model.CaracteristicaDetalhe
import com.lacos_preciosos.preciososLacos.repository.CaracteristicaDetalheRepository
import com.lacos_preciosos.preciososLacos.repository.CaracteristicaRepository
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.springframework.stereotype.Service

@Service
class CaracteristicaDetalheService(
    private val caracteristicaDetalheRepository: CaracteristicaDetalheRepository,
    private val caracteristicaRepository: CaracteristicaRepository
) {


}