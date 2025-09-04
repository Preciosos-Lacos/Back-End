package com.lacos_preciosos.preciososLacos.model

import com.lacos_preciosos.preciososLacos.dto.modelo.CadastroModeloDTO
import jakarta.persistence.*
import java.util.Base64

@Entity
data class Modelo(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idModelo: Int? = null,

    @Column(name = "nome_modelo")
    var nomeModelo: String = "",

    var preco: Double = 0.0,

    var descricao: String = "",

    var favorito: Boolean?,

    @Lob
    @Column(name = "foto", columnDefinition = "LONGBLOB")
    var foto: ByteArray? = null,

    @ManyToMany
    @JoinTable(
        name = "favorito_Modelo",
        joinColumns = [JoinColumn(name = "idModelo")],
        inverseJoinColumns = [JoinColumn(name = "idUsuario")]
    )

    var listaUsuario: List<Usuario>? = null

) {
    constructor(dto: CadastroModeloDTO, idModelo: Int, nomeModelo: String, preco: Double, descricao: String) : this(
        null,
        dto.nome,
        dto.preco,
        dto.descricao,
        dto.favorito
    )

    constructor() : this(
        idModelo = null,
        nomeModelo = "",
        preco = 0.0,
        descricao = "",
        favorito = null,
        foto = null,
        listaUsuario = null
    )

    // Método para adicionar/atualizar foto
    fun adicionarFoto(imagemBase64: String) {
        this.foto = Base64.getDecoder().decode(imagemBase64)
    }

    // Método para obter a foto como Base64
    fun getFotoBase64(): String? {
        return if (foto != null) Base64.getEncoder().encodeToString(foto) else null
    }

// Sobrescrever equals e hashCode para ignorar o campo foto na comparação
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Modelo

        if (idModelo != other.idModelo) return false
        if (nomeModelo != other.nomeModelo) return false
        if (preco != other.preco) return false
        if (descricao != other.descricao) return false
        if (favorito != other.favorito) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idModelo ?: 0
        result = 31 * result + nomeModelo.hashCode()
        result = 31 * result + preco.hashCode()
        result = 31 * result + descricao.hashCode()
        result = 31 * result + (favorito?.hashCode() ?: 0)
        return result
    }
}