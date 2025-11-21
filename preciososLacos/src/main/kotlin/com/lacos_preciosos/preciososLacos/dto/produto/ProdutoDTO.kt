package com.lacos_preciosos.preciososLacos.dto.produto

data class ProdutoDTO(
    val idProduto: Int?,
    val idModelo: Int?,
    val nome: String,
    val colecao: String?,      // modelo.nomeModelo
    val tamanho: String?,
    val tipoLaco: String?,
    val material: String?,
    val cor: String?,
    val corDescricao: String?,
    val acabamento: String?,
    val acabamentoDescricao: String?,
    val foto: String?,         // base64 foto do modelo
    val preco: Double?
)
