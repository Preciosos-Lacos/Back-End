package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.Endereco
import com.lacos_preciosos.preciososLacos.model.Usuario
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface EnderecoRepository : JpaRepository<Endereco, Int> {

    // Create a new Endereco
//    @Transactional
//    fun criarEndereco(endereco: Endereco): Endereco {
//        return save(endereco)
//    }

    // Update an existing Endereco
    @Transactional
    @Modifying
    @Query("""
        update Endereco e
        set e.logradouro = :logradouro,
            e.numero = :numero,
            e.complemento = :complemento,
            e.localidade = :localidade,
            e.bairro = :bairro,
            e.cep = :cep,
            e.uf = :uf
        where e.idEndereco = :idEndereco
    """)
    fun atualizarEndereco(
        idEndereco: Int,
        logradouro: String,
        numero: Int,
        complemento: String?,
        bairro: String,
        cep: String,
        localidade: String?,
        uf: String?
    ): Int


    fun findByIdEndereco(id: Int): List<Endereco>
}