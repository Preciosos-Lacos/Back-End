package com.lacos_preciosos.preciososLacos.dto.usuario

data class UsuarioDTO(
    val idUsuario: Int?,
    val nomeCompleto: String,
    val login: String,
    val telefone: String?,
    val cpf: String?,
    val foto: String?
)

