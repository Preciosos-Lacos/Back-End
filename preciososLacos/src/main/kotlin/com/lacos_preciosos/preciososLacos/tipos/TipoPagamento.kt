package com.lacos_preciosos.preciososLacos.tipos

enum class TipoPagamento {

    DEBITO("debito"),
    CREDITO("credito");

    var tipo: String = "";

    constructor(tipo: String) {
        this.tipo = tipo
    }

    companion object{
        fun getTipoPagamento(tipo: String): TipoPagamento {
            return values().find { it.tipo.equals(tipo, ignoreCase = true) }
                ?:  throw IllegalArgumentException("Tipo de pagamento inválido")
        }
    }

}