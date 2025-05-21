package com.lacos_preciosos.preciososLacos.dto.usuario

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class AutenticacaoUsuarioDTO(
    @field:NotBlank
    val email: String,

    @field:Size(min = 8)
    val senha: String,

    val autenticacao: Boolean,
)
