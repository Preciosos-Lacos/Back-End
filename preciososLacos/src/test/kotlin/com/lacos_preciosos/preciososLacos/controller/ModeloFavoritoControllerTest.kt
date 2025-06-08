import com.lacos_preciosos.preciososLacos.controller.ModeloController
import com.lacos_preciosos.preciososLacos.dto.modelo.DadosDetalheModelo
import com.lacos_preciosos.preciososLacos.service.ModeloService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class ModeloFavoritoControllerTest {
    private lateinit var service: ModeloService

    private lateinit var controller: ModeloController

    private val modeloMock = DadosDetalheModelo(1, "Modelo Teste", 100.0, "Descrição Teste")

    @BeforeEach
    fun setup() {
        service = mock(ModeloService::class.java)
        controller = ModeloController(service)
    }

    @Test
    @DisplayName("A adição de favorito com dados válidos deve retornar status 201 com os dados do modelo")
    fun testAdicionarFavoritoComDadosValidos() {
        val dto = com.lacos_preciosos.preciososLacos.dto.modelo.CadastroFavoritoDTO(1, 1)
        `when`(service.adicionarFavorito(dto)).thenReturn(modeloMock)

        val retorno = controller.adicionarFavorito(dto)

        assertEquals(201, retorno.statusCode.value())
        assertEquals(modeloMock, retorno.body)
    }

    @Test
    @DisplayName("A adição de favorito com usuário inexistente deve retornar status 404")
    fun testAdicionarFavoritoComUsuarioInexistente() {
        val dto = com.lacos_preciosos.preciososLacos.dto.modelo.CadastroFavoritoDTO(1, 999)
        `when`(service.adicionarFavorito(dto)).thenThrow(RuntimeException("Usuário com esse ID não foi encontrado"))

        val retorno = controller.adicionarFavorito(dto)

        assertEquals(404, retorno.statusCode.value())
    }

    @Test
    @DisplayName("A adição de favorito com modelo inexistente deve retornar status 404")
    fun testAdicionarFavoritoComModeloInexistente() {
        val dto = com.lacos_preciosos.preciososLacos.dto.modelo.CadastroFavoritoDTO(999, 1)
        `when`(service.adicionarFavorito(dto)).thenThrow(RuntimeException("Modelo com esse ID não foi encontrado"))

        val retorno = controller.adicionarFavorito(dto)

        assertEquals(404, retorno.statusCode.value())
    }

    @Test
    @DisplayName("A exclusão de favorito existente deve retornar status 20")
    fun testDeleteFavoritoExistente() {
        val dto = com.lacos_preciosos.preciososLacos.dto.modelo.DeleteFavoritoDTO(999,1)
        doNothing().`when`(service).deleteFavorito(dto)

        val retorno = controller.deleteFavorito(dto)

        verify(service, times(1)).deleteFavorito(dto)
        assertEquals(204, retorno.statusCode.value())
    }
}