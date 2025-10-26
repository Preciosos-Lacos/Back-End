package com.lacos_preciosos.preciososLacos.dto.cor

import com.lacos_preciosos.preciososLacos.model.Modelo
import jakarta.validation.constraints.NotBlank

data class CadastroCorModeloDTO(@field:NotBlank
                                val id: Int,
                                val listaModelos: List<Int>
){
    companion object
}
