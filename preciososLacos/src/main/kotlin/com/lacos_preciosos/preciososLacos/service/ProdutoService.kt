package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.*
import com.lacos_preciosos.preciososLacos.dto.produto.CadastroProdutoDTO
import com.lacos_preciosos.preciososLacos.dto.produto.DadosDetalheProduto
import com.lacos_preciosos.preciososLacos.model.Produto
import com.lacos_preciosos.preciososLacos.repository.ModeloRepository
import com.lacos_preciosos.preciososLacos.repository.ProdutoRepository
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.springframework.stereotype.Service

@Service
class ProdutoService(val produtoRepository: ProdutoRepository, val modeloRepository: ModeloRepository) {

    fun getAllProdutos(): List<Produto> {
        return produtoRepository.findAll()
    }

    fun save(produtoDTO: CadastroProdutoDTO): DadosDetalheProduto {
        val modelo = modeloRepository.findById(produtoDTO.idModelo).orElseThrow {
            RuntimeException("Modelo não encontrado")
        }

        val produto = Produto(produtoDTO)
        produto.modelo = modelo
        produtoRepository.save(produto)
        return DadosDetalheProduto(produto)
    }

    fun listarProdutos(): List<Produto> {
        var produtos = produtoRepository.findAll()

        if (produtos.isEmpty()) {
            throw Exception("Nenhum produto encontrado")
        } else {
            return produtos
        }
    }

    fun updateProduto(id: Int, dto: CadastroProdutoDTO): Produto {
        val produto = produtoRepository.findById(id)
            .orElseThrow { ValidacaoException("Endereço não encontrado com ID: $id") }

        produto.cor = dto.cor
        produto.preco = dto.preco
        produto.tamanho = dto.tamanho
        produto.acabamento = dto.acabamento
        produto.tipoLaco = dto.material


        return produtoRepository.save(produto)
    }

    fun deleteModelo(id: Int) {
        var existe = produtoRepository.existsById(id)

        if (existe) {
            produtoRepository.deleteById(id)
        } else {
            throw ValidacaoException("Modelo não encontrado")
        }
    }

}