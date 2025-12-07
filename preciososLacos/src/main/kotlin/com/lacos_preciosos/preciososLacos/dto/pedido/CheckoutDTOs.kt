package com.lacos_preciosos.preciososLacos.dto.pedido

// NOTE: Avoid top-level import to prevent ordering issues; use fully-qualified type for usuario field.

// DTO para característica de um item no checkout

data class CheckoutCaracteristicaDTO(
    val nome: String,
    val detalhe: String
)

// DTO para o endereço no checkout (espelha o modelo Endereco, mas só com campos necessários)

// NOTE: Adicionado campos idEndereco e usuario para atender ao contrato do front-end.
// Campos novos: idEndereco: Int?, usuario: com.lacos_preciosos.preciososLacos.dto.usuario.UsuarioResponseDTO?

data class CheckoutEnderecoDTO(
    val idEndereco: Int? = null,
    val cep: String? = null,
    val logradouro: String? = null,
    val bairro: String? = null,
    val numero: Int? = null,
    val complemento: String? = null,
    val localidade: String? = null,
    val uf: String? = null,
    val usuario: com.lacos_preciosos.preciososLacos.dto.usuario.UsuarioResponseDTO? = null
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
    val frete: Double?,
    val idEndereco: Int?,
    val cep: String?
)
