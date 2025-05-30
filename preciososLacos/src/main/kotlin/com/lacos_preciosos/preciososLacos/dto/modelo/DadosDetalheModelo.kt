package com.lacos_preciosos.preciososLacos.dto.modelo

import com.lacos_preciosos.preciososLacos.model.Modelo
import lombok.NoArgsConstructor

@NoArgsConstructor
data class DadosDetalheModelo(
    var idModelo : Int?,
    var nomeModelo: String,
    var preco: Double,
    var descricao: String

) {
   constructor(modelo: Modelo): this(
       modelo.idModelo,
       modelo.nomeModelo,
       modelo.preco,
       modelo.descricao

   )
}
