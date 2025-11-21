package com.lacos_preciosos.preciososLacos.model

import com.lacos_preciosos.preciososLacos.dto.produto.CadastroProdutoDTO
import jakarta.persistence.*

@Entity
data class Produto(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idProduto: Int? = null,

    var tamanho: String = "",

    @Column(name = "cor", columnDefinition = "VARCHAR(255)")
    var cor: String? = null,

    @Column(name = "tipo_laco")
    var tipoLaco: String = "",

    @Column(name = "acabamento", columnDefinition = "VARCHAR(255)")
    var acabamento: String? = null,

    var preco: Double = 0.0,

    @ManyToOne
    var modelo: Modelo? = null,

    val nome: String = "",

    val material: String = ""

) {

    constructor(dto: CadastroProdutoDTO) : this(
        null,
        dto.tamanho,
        dto.cor,
        dto.material,
        dto.acabamento,
        dto.preco,
        null,
        dto.nome,
        dto.material
    )


}