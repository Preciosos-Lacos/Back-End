package com.lacos_preciosos.preciososLacos.dto

import com.lacos_preciosos.preciososLacos.model.Modelo
import lombok.NoArgsConstructor

@NoArgsConstructor
data class DadosDetalheModelo(
    var idModelo : Int?,
    var nomeModelo: String,
    var preco: Double,
    var descricao: String,
    var foto: String
) {
   constructor(modelo: Modelo): this(
       modelo.idModelo,
       modelo.nomeModelo,
       modelo.preco,
       modelo.descricao,
       modelo.foto
   )
}
