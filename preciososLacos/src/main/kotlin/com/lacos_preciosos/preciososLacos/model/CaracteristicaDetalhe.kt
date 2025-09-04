package com.lacos_preciosos.preciososLacos.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import java.util.Base64

@Entity
data class CaracteristicaDetalhe(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:NotBlank
    var idCaracteristicaDetalhe: Int? = null,
    
    @field:NotBlank
    var descricao: String? = null,
    
    @ManyToOne
    var caracteristica: Caracteristica? = null,

    @Lob
    @Column(name = "imagem", columnDefinition = "LONGBLOB")
    var imagem: ByteArray? = null
    
) {
    // Método para adicionar/atualizar imagem
    fun adicionarImagem(imagemBase64: String) {
        this.imagem = Base64.getDecoder().decode(imagemBase64)
    }

    // Método para obter a imagem como Base64
    fun getImagemBase64(): String? {
        return if (imagem != null) Base64.getEncoder().encodeToString(imagem) else null
    }

    // Sobrescrever equals e hashCode para ignorar o campo imagem na comparação
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CaracteristicaDetalhe

        if (idCaracteristicaDetalhe != other.idCaracteristicaDetalhe) return false
        if (descricao != other.descricao) return false
        if (caracteristica != other.caracteristica) return false

        return true
    }

    override fun hashCode(): Int {
        var result = idCaracteristicaDetalhe ?: 0
        result = 31 * result + (descricao?.hashCode() ?: 0)
        result = 31 * result + (caracteristica?.hashCode() ?: 0)
        return result
    }
}