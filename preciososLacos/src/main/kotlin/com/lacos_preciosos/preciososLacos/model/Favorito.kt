package com.lacos_preciosos.preciososLacos.model

import com.lacos_preciosos.preciososLacos.dto.favorito.CadastroFavoritoDTO
import jakarta.persistence.*

@Entity
@Table(name = "favorito")
data class Favorito(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    var tamanho: String = "",

    var cor: String = "",

    @Column(name = "tipo_laco")
    var tipoLaco: String = "",

    var acabamento: String = "",

    var preco: Double = 0.0,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id")
    var usuario: Usuario? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("produtoId")
    @JoinColumn(name = "produto_id")
    var produto: Produto? = null,
    var idFavorito: Int
){
    constructor(dto: CadastroFavoritoDTO) : this(
        id = if (dto.idFavorito != 0) dto.idFavorito else null,
        tamanho = dto.tamanho,
        cor = dto.cor,
        tipoLaco = dto.tipoLaco,
        acabamento = dto.acabamento,
        preco = dto.preco,
    )
}
