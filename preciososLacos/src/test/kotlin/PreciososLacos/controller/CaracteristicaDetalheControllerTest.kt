package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.model.Caracteristica
import com.lacos_preciosos.preciososLacos.model.CaracteristicaDetalhe
import com.lacos_preciosos.preciososLacos.repository.CaracteristicaDetalheRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.web.util.UriComponentsBuilder

class CaracteristicaDetalheControllerTest {

    private val repository = mock(CaracteristicaDetalheRepository::class.java)
    private lateinit var controller: CaracteristicaDetalheController
    private lateinit var caracteristica: Caracteristica
    private lateinit var detalhe: CaracteristicaDetalhe

    @BeforeEach
    fun setup() {
        controller = CaracteristicaDetalheController(repository)
        caracteristica = Caracteristica(idCaracteristica = 1, descricao = "COR")
        detalhe = CaracteristicaDetalhe(
            idCaracteristicaDetalhe = 1,
            descricao = "Vermelho",
            caracteristica = caracteristica
        )
    }

    @Test
    fun `criarCaracteristicaDetalhe deve retornar 201 e detalhe criado`() {
        `when`(repository.create(detalhe)).thenReturn(detalhe)
        val uriBuilder = UriComponentsBuilder.newInstance()

        val response = controller.createCaracteristicaDetalhe(detalhe, uriBuilder)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(detalhe, response.body)
    }

    @Test
    fun `getAll deve retornar 200 e lista de detalhes`() {
        `when`(repository.getAll()).thenReturn(listOf(detalhe))

        val response = controller.getAll()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(1, response.body?.size)
        assertEquals(detalhe, response.body?.first())
    }

    @Test
    fun `atualizar deve retornar 200 e detalhe atualizado`() {
        val atualizado = detalhe.copy(descricao = "Azul")
        `when`(repository.update(1, atualizado)).thenReturn(atualizado)

        val response = controller.update(1, atualizado)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(atualizado, response.body)
    }

    @Test
    fun `deletar deve retornar 404 sempre`() {
        // According to your controller, delete always returns 404
        val response = controller.delete(1)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }
}