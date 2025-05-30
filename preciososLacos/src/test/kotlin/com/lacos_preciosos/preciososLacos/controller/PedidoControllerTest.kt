import com.lacos_preciosos.preciososLacos.controller.PedidoController
import com.lacos_preciosos.preciososLacos.dto.pedido.DadosDetalhePedido
import com.lacos_preciosos.preciososLacos.service.PedidoService
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class PedidoControllerTest {

    private lateinit var service: PedidoService

    private lateinit var controller: PedidoController

    private val pedidoMock = DadosDetalhePedido(100.0, "CREDITO")

    @BeforeEach
    fun setup() {
        service = mock(PedidoService::class.java)
        controller = PedidoController(service)
    }

    @Test
    @DisplayName("A consulta de todos os pedidos com dados deve retornar status 200 com lista")
    fun testGetAllPedidos() {
        `when`(service.getAllPedidos()).thenReturn(mutableListOf(pedidoMock))

        val retorno = controller.getAllPedidos()

        assertEquals(200, retorno.statusCode.value())
        assertEquals(1, retorno.body?.size)
    }

    @Test
    @DisplayName("A consulta de todos os pedidos sem dados deve retornar status 204")
    fun testGetAllPedidosSemCorpo() {
        `when`(service.getAllPedidos()).thenThrow(RuntimeException())

        val retorno = controller.getAllPedidos()

        assertEquals(204, retorno.statusCode.value())
    }

    @Test
    @DisplayName("A consulta por ID de pedido existente deve retornar status 200 com os dados")
    fun testGetPedidoPorIdExistente() {
        `when`(service.getOnePedido(1)).thenReturn(pedidoMock)

        val retorno = controller.getOneModelo(1)

        assertEquals(200, retorno.statusCode.value())
        assertEquals(pedidoMock, retorno.body)
    }

    @Test
    @DisplayName("A consulta por ID de pedido inexistente deve retornar status 404")
    fun testGetPedidoPorIdInexistente() {
        `when`(service.getOnePedido(999)).thenThrow(RuntimeException())

        val retorno = controller.getOneModelo(999)

        assertEquals(404, retorno.statusCode.value())
    }

    @Test
    @DisplayName("A exclusão de pedido existente deve retornar status 204")
    fun testDeletePedidoExistente() {
        val retorno = controller.deletePedido(1)

        verify(service, times(1)).deletePedido(1)
        assertEquals(204, retorno.statusCode.value())
    }

    @Test
    @DisplayName("A exclusão de pedido inexistente deve retornar status 404 e não chamar o service")
    fun testDeletePedidoInexistente() {

        `when`(service.deletePedido(999)).thenThrow(ValidacaoException("Pedido não encontrado"))

        val retorno = controller.deletePedido(999)

        verify(service, times(1)).deletePedido(999)
        assertEquals(404, retorno.statusCode.value())
    }
}
