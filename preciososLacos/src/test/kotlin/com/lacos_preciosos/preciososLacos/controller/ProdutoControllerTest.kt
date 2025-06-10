package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.dto.produto.CadastroProdutoDTO
import com.lacos_preciosos.preciososLacos.dto.produto.DadosDetalheProduto
import com.lacos_preciosos.preciososLacos.model.Produto
import com.lacos_preciosos.preciososLacos.service.ProdutoService
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class ProdutoControllerTest {

    private lateinit var service: ProdutoService
    private lateinit var controller: ProdutoController

    private val produtoMock = Produto(1, "G", "Azul", "Cetim", "Azul Claro", 10.0)
    private val dadosDetalheMock = DadosDetalheProduto(produtoMock)

    @BeforeEach
    fun setup() {
        service = mock(ProdutoService::class.java)
        controller = ProdutoController(service)
    }

    @Test
    @DisplayName("A consulta de todos os produtos com dados deve retornar status 200 com lista")
    fun testListarProdutosComDados() {
        `when`(service.listarProdutos()).thenReturn(mutableListOf(produtoMock))

        val response = controller.listarProdutos()

        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)
        assertEquals(produtoMock, response.body?.get(0))
    }

    @Test
    @DisplayName("A consulta de todos os produtos sem dados deve retornar status 204")
    fun testListarProdutosSemDados() {
        `when`(service.listarProdutos()).thenThrow(ValidacaoException(""))

        val response = controller.listarProdutos()

        assertEquals(204, response.statusCode.value())
    }

    @Test
    @DisplayName("Deve retornar status 201 ao cadastrar produto válido")
    fun testCadastrarProdutoValido() {
        val novoProduto = CadastroProdutoDTO("Laço Bonito", "G", "Cetim", "Azul Claro", "Brilhante", 10.0, 1)
        `when`(service.save(novoProduto)).thenReturn(dadosDetalheMock)

        val response = controller.cadastrarProduto(novoProduto)

        assertEquals(201, response.statusCode.value())
        assertEquals(novoProduto.tamanho, response.body?.tamanho)
    }

    @Test
    @DisplayName("Deve retornar status 200 ao atualizar produto existente")
    fun testAtualizarProdutoExistente() {
        val dto = CadastroProdutoDTO("Laço Novo", "M", "Gorgurão", "Branco", "Opaco", 15.0, 1)
        `when`(service.updateProduto(1, dto)).thenReturn(produtoMock)

        val response = controller.updateProduto(1, dto)

        assertEquals(200, response.statusCode.value())
        assertEquals(produtoMock, response.body)
    }

    @Test
    @DisplayName("Deve retornar status 404 ao tentar atualizar produto inexistente")
    fun testAtualizarProdutoInexistente() {
        val dto = CadastroProdutoDTO("Laço Inexistente", "P", "Veludo", "Prata", "Fosco", 12.0, 1)
        `when`(service.updateProduto(99, dto)).thenThrow(ValidacaoException("Produto não encontrado"))

        val response = controller.updateProduto(99, dto)

        assertEquals(404, response.statusCode.value())
    }

    @Test
    @DisplayName("Deve retornar status 204 ao excluir produto existente")
    fun testExcluirProdutoExistente() {
        // mocka que a exclusão não lança exceção
        doNothing().`when`(service).deleteModelo(1)

        val response = controller.deleteProduto(1)

        assertEquals(204, response.statusCode.value())
    }

    @Test
    @DisplayName("Deve retornar status 404 ao tentar excluir produto inexistente")
    fun testExcluirProdutoInexistente() {
        doThrow(ValidacaoException("Produto não encontrado")).`when`(service).deleteModelo(99)

        val response = controller.deleteProduto(99)

        assertEquals(404, response.statusCode.value())
    }
}
