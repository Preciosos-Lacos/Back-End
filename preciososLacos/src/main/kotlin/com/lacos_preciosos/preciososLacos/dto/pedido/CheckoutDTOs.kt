package com.lacos_preciosos.preciososLacos.dto.pedido

// DTO para característica de um item no checkout

data class CheckoutCaracteristicaDTO(
    val nome: String,
    val detalhe: String
)

// DTO para o endereço no checkout (espelha o modelo Endereco, mas só com campos necessários)

data class CheckoutEnderecoDTO(
    val logradouro: String?,
    val numero: Int?,
    val complemento: String?,
    val bairro: String?,
    val localidade: String?,
    val uf: String?,
    val cep: String?
)

// DTO para item de produto no checkout

data class CheckoutItemDTO(
    val idProduto: Int,
    val nome: String?,
    val modelo: String?,
    val quantidade: Int,
    val precoUnitario: Double,
    val precoTotal: Double,
    val imagemPrincipal: String?,
    val caracteristicas: List<CheckoutCaracteristicaDTO>
)

// DTO principal de resumo de checkout

data class CheckoutResumoDTO(
    val idPedido: Int,
    val carrinho: Boolean,
    val produtos: List<CheckoutItemDTO>,
    val endereco: CheckoutEnderecoDTO?,
    val subtotal: Double,
    val frete: Double,
    val total: Double,
    val formasPagamento: List<String>
)

// DTO de request para finalizar pedido

data class FinalizarPedidoRequest(
    val idUsuario: Int?,
    val formaPagamento: Int?,
    val frete: Double?
)
