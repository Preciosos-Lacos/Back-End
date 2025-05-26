package com.lacos_preciosos.preciososLacos.model

import com.lacos_preciosos.preciososLacos.dto.produto.CadastroProdutoDTO
import jakarta.persistence.*

@Entity
data class Favorito @JvmOverloads constructor(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idProduto: Int?
) {

    constructor(dto: CadastroProdutoDTO) : this(
        null,
    )

}