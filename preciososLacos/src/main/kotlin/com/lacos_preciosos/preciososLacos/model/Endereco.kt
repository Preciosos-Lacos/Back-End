package com.lacos_preciosos.preciososLacos.model

import com.lacos_preciosos.preciososLacos.dto.endereco.CadastroEnderecoDTO
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank
import jakarta.persistence.*
import jakarta.validation.constraints.*

@Entity
data class Endereco (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idEndereco: Int? = null,

    @field:NotBlank
    @field:Size(min = 8, max = 8)
    var cep: String = "",

    @field:NotBlank
    var logradouro: String = "",

    @field:NotBlank
    var bairro: String = "",

    @field:NotNull
    @field:Min(1)
    var numero: Int = 0,

    var complemento: String? = null,

    @field:NotBlank
    var localidade: String? = "",

    @field:NotBlank
    var uf: String? = "",

    @ManyToOne
    var usuario: Usuario? = null


) {
    constructor(dto: CadastroEnderecoDTO, usuario: Usuario) : this(
        null,
        dto.cep,
        dto.logradouro,
        dto.bairro,
        dto.numero,
        dto.complemento,
        dto.localidade,
        dto.uf,
        usuario
    )
}