package com.lacos_preciosos.preciososLacos.dto.usuario

import java.time.LocalDate

// DTO de resposta com os campos necessários para o front-end (sem senha)

data class UsuarioResponseDTO(
    val idUsuario: Int?,
    val nomeCompleto: String,
    val login: String,
    val senha: String? = null,
    val cpf: String? = null,
    val telefone: String? = null,
    val role: String? = null,
    val data_cadastro: String? = null,
    val fotoPerfil: String? = null
)

