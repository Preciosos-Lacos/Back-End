package com.lacos_preciosos.preciososLacos.dto.endereco

interface EnderecoDTO {
    val cep: String
    val logradouro: String
    val bairro: String
    val numero: Int
    val complemento: String?
    val cidade: String
    val estado: String
    val usuarioId: Int
}