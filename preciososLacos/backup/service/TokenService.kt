package com.lacos_preciosos.preciososLacos.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.lacos_preciosos.preciososLacos.model.Usuario
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class TokenService {
    private val secret = "laços_preciosos" // Sua chave secreta

    fun gerarToken(usuario: Usuario): String {
        return try {
            val algoritmo = Algorithm.HMAC256(secret)
            JWT.create()
                .withIssuer("API Lacos_Preciosos")
                .withSubject(usuario.login)
                .withExpiresAt(dataExpiracao())
                .sign(algoritmo)
        } catch (exception: JWTCreationException) {
            throw RuntimeException("Erro ao gerar token JWT", exception)
        }
    }

    fun getSubject(tokenJWT: String): String {
        return try {
            val algoritmo = Algorithm.HMAC256(secret)
            JWT.require(algoritmo)
                .withIssuer("API Lacos_Preciosos")
                .build()
                .verify(tokenJWT)
                .subject
        } catch (exception: JWTVerificationException) {
            throw RuntimeException("Token JWT inválido ou expirado!")
        }
    }

    private fun dataExpiracao(): Instant {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"))
    }
}
