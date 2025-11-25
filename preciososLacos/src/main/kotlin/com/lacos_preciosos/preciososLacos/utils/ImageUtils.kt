package com.lacos_preciosos.preciososLacos.utils

import java.sql.Blob
import java.util.Base64

object ImageUtils {

    /**
     * Converte diferentes formas de valor de imagem retornadas por queries (ByteArray, java.sql.Blob, String) para Base64.
     * Retorna null se não houver imagem ou se o valor for de tipo não reconhecido.
     */
    fun toBase64(value: Any?): String? {
        return when (value) {
            null -> null
            is ByteArray -> Base64.getEncoder().encodeToString(value)
            is Blob -> {
                val len = value.length().toInt()
                if (len <= 0) return null
                val bytes = value.getBytes(1, len)
                Base64.getEncoder().encodeToString(bytes)
            }
            is String -> {
                val s = value.trim()
                return if (s.startsWith("data:") && s.contains("base64,")) {
                    s.substringAfter("base64,")
                } else {
                    // validação simples: caracteres válidos de Base64
                    val base64Regex = Regex("^[A-Za-z0-9+/=\\r\\n]+$")
                    if (base64Regex.matches(s)) s else null
                }
            }
            else -> null
        }
    }

    /**
     * Retorna um data URI (ex.: data:image/png;base64,...) opcionalmente usando o mimeType informado.
     */
    fun toDataUri(base64: String?, mimeType: String = "image/png"): String? {
        return base64?.let { "data:$mimeType;base64,$it" }
    }
}

