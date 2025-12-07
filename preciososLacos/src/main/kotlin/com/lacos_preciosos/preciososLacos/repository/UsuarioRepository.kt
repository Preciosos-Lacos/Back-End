package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.Usuario
import jakarta.transaction.Transactional
import jakarta.validation.constraints.Email
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.security.core.userdetails.UserDetails

interface UsuarioRepository: JpaRepository<Usuario, Int> {

    fun findByLogin(login: String?): UserDetails?

    @Transactional
    @Modifying
    @Query("update Usuario u set u.nomeCompleto = ?2, u.login = ?3, u.senha = ?4, u.telefone = ?5, u.cpf = ?6 where u.id = ?1")
    fun AtualizarUsuarios(id: Int): Int

    fun findByNomeCompletoContains(nome: String): List<Usuario>
    
    @Transactional
    @Modifying
    @Query("UPDATE Usuario u SET u.fotoPerfil = :foto WHERE u.idUsuario = :idUsuario")
    fun updateFotoPerfil(idUsuario: Int?, foto: ByteArray): Int

    @Query("SELECT * FROM usuario WHERE login = :email", nativeQuery = true)
    fun findByEmail(email: String): Usuario

    @Transactional
    @Modifying
    @Query("UPDATE usuario u SET u.password = :senha WHERE u.login = :email", nativeQuery = true)
    fun atualizarSenha(email: String, senha: String): Int

    @Transactional
    @Modifying
    @Query("UPDATE usuario u SET u.nome_completo = :nome, u.telefone = :telefone, u.cpf = :cpf, u.login = :email, u.password = :senha WHERE u.login = :login", nativeQuery = true)
    fun updateDados(nome: String, telefone: String, cpf: String, email: String, senha: String, login: String): Int

    @Transactional
    @Modifying
    @Query("UPDATE usuario u SET u.nome_completo = :nome WHERE u.login = :email", nativeQuery = true)
    fun updateNome(nome: String, email: String): Int

    @Transactional
    @Modifying
    @Query("UPDATE usuario u SET u.telefone = :telefone WHERE u.login = :email", nativeQuery = true)
    fun updateTelefone(telefone: String, email: String): Int

    @Transactional
    @Modifying
    @Query("UPDATE usuario u SET u.cpf = :cpf WHERE u.login = :email", nativeQuery = true)
    fun updateCpf(cpf: String, email: String): Int

    @Transactional
    @Modifying
    @Query("UPDATE usuario u SET u.login = :email WHERE u.login = :login", nativeQuery = true)
    fun updateEmail(email: String, login: String): Int

    @Transactional
    @Modifying
    @Query("UPDATE usuario u SET u.password = :senha WHERE u.login = :login", nativeQuery = true)
    fun updateSenha(senha: String, login: String): Int

    @Transactional
    @Modifying
    @Query("UPDATE usuario u SET u.foto_perfil = :foto WHERE u.id_usuario = :idUsuario", nativeQuery = true)
    fun updateFoto(idUsuario: Int?, foto: ByteArray): Int
}


