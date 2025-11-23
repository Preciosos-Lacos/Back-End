package com.lacos_preciosos.preciososLacos.dto.endereco

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Min

data class CadastroEnderecoDTO(
    @field:NotBlank
    @field:Size(min = 8, max = 8)
    val cep: String,

    @field:NotBlank
    val logradouro: String,

    @field:NotBlank
    val bairro: String,

    @field:NotNull
    @field:Min(1)
    val numero: Int,

    val complemento: String? = null,

    @field:NotBlank
    val localidade: String,

    @field:NotBlank
    val uf: String,

    @field:NotNull
    @field:Min(1)
    val usuarioId: Int
)