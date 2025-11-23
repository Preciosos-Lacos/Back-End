package com.lacos_preciosos.preciososLacos.infra.security

import com.lacos_preciosos.preciososLacos.repository.UsuarioRepository
import com.lacos_preciosos.preciososLacos.service.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class SecurityFilter(
    private val tokenService: TokenService,
    private val usuarioRepository: UsuarioRepository
) : OncePerRequestFilter() {

    private val rotasPublicas = listOf(
        "/usuarios/login",
        "/usuarios",
        "/caracteristica-detalhe/cor",
        "/modelos",
        "/corModelo",
        "/dashboard",
        "/produtos",
        "/produtos/promocoes",
        "/produtos/destaques",
        "/banners",
        "/banners/ativo/home",
        "/banners/upload",
        "/uploads",
        "/enderecos",
        "/enderecos/"
    )

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.servletPath

        // Permitir OPTIONS (CORS preflight)
        if (request.method == HttpMethod.OPTIONS.name()) {
            filterChain.doFilter(request, response)
            return
        }

        // Ignorar JWT para rotas públicas
        if (rotasPublicas.any { path.startsWith(it) }) {
            filterChain.doFilter(request, response)
            return
        }

        val tokenJWT = recuperarToken(request)
        if (tokenJWT == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token ausente")
            return
        }

        val subject = tokenService.getSubject(tokenJWT)
        val usuario = usuarioRepository.findByLogin(subject)

        if (usuario != null) {
            val authentication =
                UsernamePasswordAuthenticationToken(usuario, null, usuario.authorities)
            SecurityContextHolder.getContext().authentication = authentication
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Usuário inválido")
            return
        }

        filterChain.doFilter(request, response)
    }

    private fun recuperarToken(request: HttpServletRequest): String? {
        val authorizationHeader = request.getHeader("Authorization")
        return if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            authorizationHeader.substring(7)
        } else null
    }
}
