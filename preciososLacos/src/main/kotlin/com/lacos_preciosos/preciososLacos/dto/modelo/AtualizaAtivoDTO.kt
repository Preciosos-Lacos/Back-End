package com.lacos_preciosos.preciososLacos.dto.modelo

import jakarta.validation.constraints.NotNull

data class AtualizaAtivoDTO(
    @field:NotNull
    val ativo: Boolean
)

