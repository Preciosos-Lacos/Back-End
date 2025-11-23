package com.lacos_preciosos.preciososLacos.dto.modelo

import com.lacos_preciosos.preciososLacos.model.Modelo
import java.util.Base64

data class DadosDetalheModelo(
    val idModelo: Int?,
    val nomeModelo: String,
    val preco: Double,
    val descricao: String,
    val favorito: Boolean?,
    val ativo: Boolean?,
    val fotoBase64: String?
)
{
    constructor(modelo: Modelo) : this(
        idModelo = modelo.idModelo,
        nomeModelo = modelo.nomeModelo,
        preco = modelo.preco,
        descricao = modelo.descricao,
        favorito = modelo.favorito,
        ativo = modelo.ativo,
        fotoBase64 = modelo.foto?.let { Base64.getEncoder().encodeToString(it) }
    )
}
