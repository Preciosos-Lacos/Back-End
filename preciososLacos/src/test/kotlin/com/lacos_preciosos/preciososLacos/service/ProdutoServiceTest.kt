package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.model.Produto
import com.lacos_preciosos.preciososLacos.repository.ProdutoRepository
//import org.hamcrest.CoreMatchers.any
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.ArgumentMatchers.any
import java.util.*

class ProdutoServiceTest {

    private val produtoRepository = mock(ProdutoRepository::class.java)
    private val modeloRepository = mock(com.lacos_preciosos.preciososLacos.repository.ModeloRepository::class.java)

    private val dto = com.lacos_preciosos.preciososLacos.dto.produto.CadastroProdutoDTO(
        "Azul",
        "G",
        "Cetim",
        "Azul Claro",
        "Bico de Pato",
        10.0,
        1 // ID do modelo
    )

    private lateinit var service: ProdutoService
    @BeforeEach

    fun setup() {
        service = ProdutoService(produtoRepository, modeloRepository)
    }

    @Test
    @DisplayName ("Testar se retorna todos os produtos")
    fun testGetAllProdutos() {
        val produto = Produto(dto)

        `when`(produtoRepository.findAll()).thenReturn(listOf(produto))

        val resultado = service.getAllProdutos()

        assertEquals(1, resultado.size)
        assertEquals("Azul", resultado[0].cor)

    }
    @Test
    @DisplayName("Testar se salva um produto corretamente")
    fun testSaveProduto() {

        val produto = Produto(dto)
//        `when`(modeloRepository.findById(dto.idModelo)).thenReturn(java.util.Optional.of(com.lacos_preciosos.preciososLacos.model.Modelo(1, "Azul")))
        `when`(modeloRepository.findById(dto.idModelo)).thenReturn(
            Optional.of(com.lacos_preciosos.preciososLacos.model.Modelo(
                idModelo = 1,
                nomeModelo = "Azul",
                preco = 10.0,
                descricao = "Descrição do modelo",
                favorito = null,
                listaUsuario = null
            ))
        )
        `when`(produtoRepository.save(produto)).thenReturn(produto)

        val resultado = service.save(dto)

        assertEquals("Azul", resultado.cor)
        assertEquals("G", resultado.tamanho)
        assertEquals("Cetim", resultado.tipoLaco)
        assertEquals("Azul Claro", resultado.acabamento)
        assertEquals(10.0, resultado.preco)
    }

    @Test
    @DisplayName("A consulta de todos os produtos sem dados deve lançar exceção")
    fun testGetAllProdutosSemDados() {
        `when`(produtoRepository.findAll()).thenReturn(emptyList())

        assertThrows(Exception::class.java) {
            service.listarProdutos()
        }
    }


    @Test
    @DisplayName("Salvar produto com modelo existente deve funcionar corretamente")
    fun testSaveProdutoComModeloExistente() {
        val modelo = mock(com.lacos_preciosos.preciososLacos.model.Modelo::class.java)
        `when`(modeloRepository.findById(1)).thenReturn(Optional.of(modelo))

        val produtoSalvo = Produto(dto)
        produtoSalvo.modelo = modelo
        `when`(produtoRepository.save(any(Produto::class.java))).thenReturn(produtoSalvo)

        val resultado = service.save(dto)

        assertEquals("Azul", resultado.cor)
        assertEquals("G", resultado.tamanho)
    }
    @Test
    @DisplayName("Salvar produto com modelo inexistente deve lançar exceção")
    fun testSaveProdutoComModeloInexistente() {
        `when`(modeloRepository.findById(dto.idModelo)).thenReturn(Optional.empty())

        assertThrows(RuntimeException::class.java) {
            service.save(dto)
        }
    }




    //GITHUB COPILOT SUGGESTIONS // CHATGPT SUGGESTIONS


    
@Test
    @DisplayName("Atualizar produto existente deve funcionar corretamente")
    fun testUpdateProduto() {
        val produtoExistente = Produto(dto)
        `when`(produtoRepository.findById(1)).thenReturn(Optional.of(produtoExistente))

        val dtoAtualizado = com.lacos_preciosos.preciososLacos.dto.produto.CadastroProdutoDTO(
            "Vermelho",
            "M",
            "Seda",
            "Vermelho Claro",
            "Laço de Cabelo",
            15.0,
            1
        )

        val produtoAtualizado = service.updateProduto(1, dtoAtualizado)

        assertEquals("Vermelho", produtoAtualizado.cor)
        assertEquals("M", produtoAtualizado.tamanho)
        assertEquals("Seda", produtoAtualizado.tipoLaco)
        assertEquals("Vermelho Claro", produtoAtualizado.acabamento)
        assertEquals(15.0, produtoAtualizado.preco)
    }

    @Test
    @DisplayName("Deletar produto existente deve funcionar corretamente")
    fun testDeleteProduto() {
        `when`(produtoRepository.existsById(1)).thenReturn(true)

        service.deleteModelo(1)

        // Verifica se o método deleteById foi chamado
        org.mockito.Mockito.verify(produtoRepository).deleteById(1)
    }

    @Test
    @DisplayName ("Buscar produto por ID existente deve retornar os dados corretamente")
    fun testGetProdutoPorIdExistente() {
        val produto = Produto(dto)
        `when`(produtoRepository.findById(1)).thenReturn(Optional.of(produto))

        val resultado = service.getAllProdutos().find { it.idProduto == 1 }


        assertEquals("Azul", resultado?.cor)
        assertEquals("G", resultado?.tamanho)
    }
}