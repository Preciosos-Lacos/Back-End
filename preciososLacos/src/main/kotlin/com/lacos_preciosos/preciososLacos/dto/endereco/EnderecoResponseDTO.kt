package com.lacos_preciosos.preciososLacos.dto.endereco

import com.lacos_preciosos.preciososLacos.dto.usuario.UsuarioResponseDTO

data class EnderecoResponseDTO(
    val idEndereco: Int?,
    val cep: String,
    val logradouro: String,
    val bairro: String,
    val numero: Int,
    val complemento: String?,
    val localidade: String,
    val uf: String,
    val usuario: UsuarioResponseDTO?
)

