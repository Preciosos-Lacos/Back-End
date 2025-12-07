package com.lacos_preciosos.preciososLacos.dto

data class PedidoResumoDTO(
    val id: Int,
    val total: String,
    val formaPagamento: String,
    val formaEnvio: String,
    // val cepEntrega removido: agora o endereço é obtido via relacionamento
)
