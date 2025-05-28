package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.dto.endereco.CadastroEnderecoDTO
import com.lacos_preciosos.preciososLacos.model.Endereco
import com.lacos_preciosos.preciososLacos.model.Usuario
import com.lacos_preciosos.preciososLacos.service.EnderecoService
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.util.UriComponentsBuilder

class EnderecoControllerTest {

    private val service = mock(EnderecoService::class.java)
    private lateinit var controller: EnderecoController
    private lateinit var endereco: Endereco
    private lateinit var usuario: Usuario

    @BeforeEach
    fun setup() {
        controller = EnderecoController(service)
        usuario = Usuario(idUsuario = 1)
        endereco = Endereco(
            idEndereco = 1,
            cep = "12345678",
            logradouro = "Rua Teste",
            bairro = "Centro",
            numero = 100,
            complemento = "Apto 1",
            localidade = "Cidade",
            uf = "SP",
            usuario = usuario
        )
    }

    @Test
    fun `criarEndereco deve retornar 201 e endereco criado`() {
        val dto = CadastroEnderecoDTO("12345678", "Rua Teste", "Centro", 100, "Apto 1", "Cidade", "SP", usuarioId = 1)
        `when`(service.createEndereco(dto, 1)).thenReturn(endereco)
        val uriBuilder = UriComponentsBuilder.newInstance()

        val response = controller.createEndereco(dto, uriBuilder)

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(endereco, response.body)
        assertTrue(response.headers.location.toString().contains("/enderecos/1"))
    }

    @Test
    fun `buscarEnderecoPorId deve retornar 200 e endereco quando encontrado`() {
        `when`(service.getEnderecoById(1)).thenReturn(endereco)

        val response = controller.getEnderecoById(1)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(endereco, response.body)
    }

    @Test
    fun `buscarEnderecoPorId deve retornar 404 quando nao encontrado`() {
        `when`(service.getEnderecoById(9)).thenThrow(ValidacaoException("Endereço não encontrado"))

        val response = controller.getEnderecoById(9)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun `atualizarEndereco deve retornar 200 e endereco atualizado`() {
        val dto = CadastroEnderecoDTO("54321000", "Rua Nova", "Bairro", 200, "Casa", "Outra Cidade", "RJ", usuarioId = 1)
        `when`(service.updateEndereco(1, dto)).thenReturn(endereco)

        val response = controller.updateEndereco(1, dto)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(endereco, response.body)
    }

    @Test
    fun `atualizarEndereco deve retornar 404 quando nao encontrado`() {
        val dto = CadastroEnderecoDTO("54321000", "Rua Nova", "Bairro", 200, "Casa", "Outra Cidade", "RJ", usuarioId = 1)
        `when`(service.updateEndereco(9, dto)).thenThrow(ValidacaoException("Endereço não encontrado"))

        val response = controller.updateEndereco(9, dto)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun `deletarEndereco deve retornar 204 quando encontrado`() {
        doNothing().`when`(service).deleteEndereco(1)

        val response = controller.deleteEndereco(1)

        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        assertNull(response.body)
    }

    @Test
    fun `deletarEndereco deve retornar 404 quando nao encontrado`() {
        doThrow(ValidacaoException("Endereço não encontrado")).`when`(service).deleteEndereco(9)

        val response = controller.deleteEndereco(9)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
    }
}