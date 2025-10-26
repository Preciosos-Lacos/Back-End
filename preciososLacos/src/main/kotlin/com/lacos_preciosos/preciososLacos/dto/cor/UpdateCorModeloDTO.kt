package com.lacos_preciosos.preciososLacos.dto.cor

import com.lacos_preciosos.preciososLacos.model.Modelo
import jakarta.validation.constraints.NotBlank

data class UpdateCorModeloDTO(@field:NotBlank
                              val modeloIdModelo: Int,
                              val caracteristicaIdDetalhe: Int
){
    companion object
}
