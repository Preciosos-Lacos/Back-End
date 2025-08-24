package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.repository.UsuarioRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AutenticacaoService(
    private val usuarioRepository: UsuarioRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails? {
        return usuarioRepository.findByLogin(username!!);
    }


}