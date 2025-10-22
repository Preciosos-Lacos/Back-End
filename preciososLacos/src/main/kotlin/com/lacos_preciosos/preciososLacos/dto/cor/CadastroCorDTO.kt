package com.lacos_preciosos.preciososLacos.dto.cor

import jakarta.validation.constraints.NotBlank

data class CadastroCorDTO(@field:NotBlank
                        val nomeDaCor: String,
                        val hexaDecimal: String,
                        val preco: Double,
                        val listaModelos: List<String>){
}
