package com.lacos_preciosos.preciososLacos.model

import com.lacos_preciosos.preciososLacos.dto.modelo.CadastroModeloDTO
import jakarta.persistence.*

@Entity
data class Modelo(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idModelo: Int? = null,

    @Column(name = "nome_modelo")
    var nomeModelo: String = "",

    var preco: Double = 0.0,

    var descricao: String = "",

) {
    constructor(dto: CadastroModeloDTO) : this(
        null,
        dto.nome,
        dto.preco,
        dto.descricao
    )


}