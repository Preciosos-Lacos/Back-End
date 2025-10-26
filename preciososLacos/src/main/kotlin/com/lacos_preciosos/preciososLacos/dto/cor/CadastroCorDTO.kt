package com.lacos_preciosos.preciososLacos.dto.cor

import com.lacos_preciosos.preciososLacos.model.Modelo
import jakarta.validation.constraints.NotBlank

data class CadastroCorDTO(@field:NotBlank
                          val id: Int?,
                          val nomeDaCor: String?,
                          val hexaDecimal: String?,
                          val preco: Double,
                          val listaModelos: List<Modelo>
){
    companion object
}
