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
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

class ModeloControllerTest {
    private lateinit var servico: ModeloService
    private lateinit var controlador: ModeloController

    private val modeloMock = Modelo(1, "Modelo Teste", 100.0, "Descrição Teste", false)
    private val modeloDetalheMock = DadosDetalheModelo(1, "Modelo Teste", 100.0, "Descrição Teste")
    private val cadastroModeloDTOMock = CadastroModeloDTO("Modelo Teste", 100.0, "Descrição Teste", false)

    @BeforeEach
    fun configurar() {
        servico = mock(ModeloService::class.java)
        controlador = ModeloController(servico)
    }

    @Test
    fun `criar Modelo deve retornar 201 e o modelo criado`() {
        `when`(servico.createModelo(cadastroModeloDTOMock)).thenReturn(modeloDetalheMock)

        val construtorUri = UriComponentsBuilder.newInstance()
        val resposta = controlador.createModelo(cadastroModeloDTOMock, construtorUri)

        assertEquals(HttpStatus.CREATED, resposta.statusCode)
        assertEquals(modeloDetalheMock, resposta.body)
        verify(servico, times(1)).createModelo(cadastroModeloDTOMock)
    }

    @Test
    fun `listar Todos Modelos deve retornar 200 e uma lista de modelos quando modelos existem`() {
        val modelos = listOf(modeloMock)
        `when`(servico.getAllModelos()).thenReturn(modelos)

        val resposta = controlador.getAllModelos()

        assertEquals(HttpStatus.OK, resposta.statusCode)
        assertEquals(modelos, resposta.body)
        verify(servico, times(1)).getAllModelos()
    }

    @Test
    fun `listar Todos Modelos deve retornar 204 quando não existem modelos`() {
        `when`(servico.getAllModelos()).thenReturn(emptyList())

        val resposta = controlador.getAllModelos()

        assertEquals(HttpStatus.NO_CONTENT, resposta.statusCode)
        assertNull(resposta.body)
        verify(servico, times(1)).getAllModelos()
    }

    @Test
    fun `buscar Um Modelo deve retornar 200 e o modelo quando ele existe`() {
        `when`(servico.getOneModelo(1)).thenReturn(Optional.of(modeloMock))
        val resposta = controlador.getOneModelo(1)

        assertEquals(HttpStatus.OK, resposta.statusCode)
        assertEquals(modeloMock, resposta.body)
        verify(servico, times(1)).getOneModelo(1)
    }

    @Test
    fun `buscar Um Modelo deve retornar 404 quando ele não existe`() {
        `when`(servico.getOneModelo(999)).thenReturn(Optional.empty())
        val resposta = controlador.getOneModelo(999)

        assertEquals(HttpStatus.NOT_FOUND, resposta.statusCode)
        assertNull(resposta.body)
        verify(servico, times(1)).getOneModelo(999)
    }

    @Test
    fun `atualizar Modelo deve retornar 200 e o modelo atualizado`() {
        `when`(servico.updateModelo(1, cadastroModeloDTOMock)).thenReturn(modeloDetalheMock)

        val resposta = controlador.updateModelo(1, cadastroModeloDTOMock)

        assertEquals(HttpStatus.OK, resposta.statusCode)
        assertEquals(modeloDetalheMock, resposta.body)
        verify(servico, times(1)).updateModelo(1, cadastroModeloDTOMock)

    }

    @Test
    fun `atualizar Modelo deve retornar 404 quando o modelo não existe`() {
        `when`(servico.updateModelo(999, cadastroModeloDTOMock)).thenThrow(ValidacaoException("Modelo não existe"))
        val resposta = controlador.updateModelo(999, cadastroModeloDTOMock)

        assertEquals(HttpStatus.NOT_FOUND, resposta.statusCode)
        verify(servico, times(1)).updateModelo(999, cadastroModeloDTOMock)
    }

    @Test
    fun `excluir Modelo deve retornar 404 quando o modelo não existe`() {
        `when`(servico.deleteModelo(999)).thenThrow(ValidacaoException("Modelo não existe"))
        val resposta = controlador.deleteModelo(999)

        assertEquals(HttpStatus.NOT_FOUND, resposta.statusCode)
        verify(servico, times(1)).deleteModelo(999)
    }

    @Test
    fun `excluir Modelo deve retornar 204 quando o modelo existe`(){
        doNothing().`when`(servico).deleteModelo(1)
        val resposta = controlador.deleteModelo(1)

        assertEquals(HttpStatus.NO_CONTENT, resposta.statusCode)
        assertNull(resposta.body)
        verify(servico, times(1)).deleteModelo(1)
    }

}