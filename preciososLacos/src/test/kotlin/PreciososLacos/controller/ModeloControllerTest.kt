package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.dto.modelo.CadastroModeloDTO
import com.lacos_preciosos.preciososLacos.dto.modelo.DadosDetalheModelo
import com.lacos_preciosos.preciososLacos.model.Modelo
import com.lacos_preciosos.preciososLacos.service.ModeloService
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

class ModeloControllerTest {

    private val service = mock(ModeloService::class.java)
    private lateinit var controller: ModeloController
    private lateinit var modelo: Modelo
    private lateinit var detalheModelo: DadosDetalheModelo

    @BeforeEach
    fun setup() {
        controller = ModeloController(service)
        modelo = Modelo(idModelo = 1, nomeModelo = "Laço Simples", preco = 10.0, descricao = "Descrição")
        detalheModelo = DadosDetalheModelo(1, "Laço Simples", 10.0, "Descrição")
    }

    @Test
    fun `criarModelo deve retornar 201 e modelo criado`() {
        val dto = CadastroModeloDTO("Laço Simples", 10.0, "Descrição")
        `when`(service.createModelo(dto)).thenReturn(detalheModelo)
        val uriBuilder = UriComponentsBuilder.newInstance()

        val response = controller.createModelo(dto, uriBuilder)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(detalheModelo, response.body)
        assertTrue(response.headers.location.toString().contains("/modelos/1"))
    }

    @Test
    fun `buscarTodosModelos deve retornar 200 e lista de modelos`() {
        `when`(service.getAllModelos()).thenReturn(listOf(modelo))

        val response = controller.getAllModelos()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size)
        assertEquals(modelo, response.body?.first())
    }

    @Test
    fun `buscarTodosModelos deve retornar 204 quando lista vazia`() {
        `when`(service.getAllModelos()).thenReturn(emptyList())

        val response = controller.getAllModelos()

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun `buscarModeloPorId deve retornar 200 e modelo quando encontrado`() {
        `when`(service.getOneModelo(1)).thenReturn(Optional.of(modelo))

        val response = controller.getOneModelo(1)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(modelo, response.body)
    }

    @Test
    fun `buscarModeloPorId deve retornar 404 quando nao encontrado`() {
        `when`(service.getOneModelo(9)).thenReturn(Optional.empty())

        val response = controller.getOneModelo(9)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun `buscarModeloPorNome deve retornar 200 e modelo quando encontrado`() {
        `when`(service.getModeloByNome("Laço Simples")).thenReturn(Optional.of(modelo))

        val response = controller.getModeloByName("Laço Simples")

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(modelo, response.body)
    }

    @Test
    fun `buscarModeloPorNome deve retornar 404 quando nao encontrado`() {
        `when`(service.getModeloByNome("Inexistente")).thenReturn(Optional.empty())

        val response = controller.getModeloByName("Inexistente")

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun `atualizarModelo deve retornar 200 e modelo atualizado`() {
        val dto = CadastroModeloDTO("Atualizado", 20.0, "Nova descrição")
        val detalheAtualizado = DadosDetalheModelo(1, "Atualizado", 20.0, "Nova descrição")
        `when`(service.updateModelo(1, dto)).thenReturn(detalheAtualizado)

        val response = controller.updateModelo(1, dto)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(detalheAtualizado, response.body)
    }

    @Test
    fun `atualizarModelo deve retornar 404 quando nao encontrado`() {
        val dto = CadastroModeloDTO("Atualizado", 20.0, "Nova descrição")
        `when`(service.updateModelo(9, dto)).thenThrow(ValidacaoException("Modelo não encontrado"))

        val response = controller.updateModelo(9, dto)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun `deletarModelo deve retornar 204 quando encontrado`() {
        doNothing().`when`(service).deleteModelo(1)

        val response = controller.deleteModelo(1)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun `deletarModelo deve retornar 404 quando nao encontrado`() {
        doThrow(ValidacaoException("Modelo não encontrado")).`when`(service).deleteModelo(9)

        val response = controller.deleteModelo(9)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
    }
}