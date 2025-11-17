package com.lacos_preciosos.preciososLacos.dto.cor

data class DadosDetalheCorDTO(
    val id: Int?,
    val nomeDaCor: String?,
    val hexaDecimal: String?,
    val preco: Double,
    val modelos: List<ModeloResumoDTO>
)
