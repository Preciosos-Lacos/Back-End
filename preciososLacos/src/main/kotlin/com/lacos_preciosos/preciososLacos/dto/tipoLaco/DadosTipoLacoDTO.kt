package com.lacos_preciosos.preciososLacos.dto.tipoLaco

data class DadosTipoLacoDTO(
    var descricao: String?,
    var preco: Double,
    var imagem: String,
    var modelos: List<String>
)
