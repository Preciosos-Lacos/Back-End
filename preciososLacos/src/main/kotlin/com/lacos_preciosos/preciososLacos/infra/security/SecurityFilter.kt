// SecurityFilter.kt
package com.lacos_preciosos.preciososLacos.infra.security

import com.lacos_preciosos.preciososLacos.repository.UsuarioRepository
import com.lacos_preciosos.preciososLacos.service.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class SecurityFilter(
    private val tokenService: TokenService,
    private val usuarioRepository: UsuarioRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val tokenJWT = recuperarToken(request)

        if (tokenJWT != null) {
            val subject = tokenService.getSubject(tokenJWT)
            val usuario = usuarioRepository.findByLogin(subject)

            // Verifica se o usuário foi encontrado antes de tentar criar a autenticação
            if (usuario != null) {
                val authentication = UsernamePasswordAuthenticationToken(usuario, null, usuario.authorities)
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        // Sempre chame o filterChain.doFilter no final, a menos que você queira
        // interromper o fluxo da requisição.
        filterChain.doFilter(request, response)
    }

    private fun recuperarToken(request: HttpServletRequest): String? {
        val authorizationHeader = request.getHeader("Authorization")
        return if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader.substring(7)
        } else {
            null
        }
    }
}