package com.lacos_preciosos.preciososLacos.tipos

enum class TipoPagamento {

    DEBITO("debito"),
    CREDITO("credito"),
    PIX("pix");

    var tipo: String = "";

    constructor(tipo: String) {
        this.tipo = tipo
    }

    companion object{
        fun getTipoPagamento(tipo: String): TipoPagamento {
            // Suporte adicional: se vier um código numérico ("1","2","3") mapear conforme CASE usado nas queries
            if (tipo.matches(Regex("^\\d+$"))) {
                return when(tipo.toInt()) {
                    1 -> DEBITO
                    2 -> CREDITO
                    3 -> PIX
                    else -> throw IllegalArgumentException("Tipo de pagamento inválido")
                }
            }
            return values().find { it.tipo.equals(tipo, ignoreCase = true) }
                ?:  throw IllegalArgumentException("Tipo de pagamento inválido")
        }
        // Overload opcional para chamada direta com Int (caso seja usado futuramente)
        fun getTipoPagamento(codigo: Int): TipoPagamento = when(codigo) {
            1 -> DEBITO
            2 -> CREDITO
            3 -> PIX
            else -> throw IllegalArgumentException("Tipo de pagamento inválido")
        }
    }

}