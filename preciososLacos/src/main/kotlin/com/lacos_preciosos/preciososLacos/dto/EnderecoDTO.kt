package com.lacos_preciosos.preciososLacos.dto

interface EnderecoDTO {
    val cep: Int
    val logradouro: String
    val bairro: String
    val numero: Int
    val complemento: String?
    val localidade: String
    val uf: String
}