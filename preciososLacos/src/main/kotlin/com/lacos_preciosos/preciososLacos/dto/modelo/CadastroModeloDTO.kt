package com.lacos_preciosos.preciososLacos.dto.modelo

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class CadastroModeloDTO(

    @field:NotBlank(message = "O nome do modelo não pode ser vazio")
    val nome: String,

    @field:Positive(message = "O preço do modelo não pode ser negativo")
    val preco: Double,

    @field:NotBlank(message = "A descrição do modelo não pode ser vazia")
    val descricao: String,

    @field:NotBlank(message = "A opção de favorito do modelo não pode ser vazia")
    val favorito: Boolean,

    ) {
}