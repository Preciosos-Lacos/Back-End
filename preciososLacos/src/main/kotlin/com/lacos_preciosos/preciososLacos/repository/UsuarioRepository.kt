package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.Usuario
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface UsuarioRepository: JpaRepository<Usuario, Int> {
    @Transactional
    @Modifying
    @Query("update Usuario u set u.nomeCompleto = ?2, u.email = ?3, u.senha = ?4, u.telefone = ?5, u.cpf = ?6 where u.id = ?1")
    fun AtualizarUsuarios(id: Int): Int

    fun findByNomeCompletoContains(nome: String): List<Usuario>
}


