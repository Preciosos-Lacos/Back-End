package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.pedido.CadastroPedidoDTO
import com.lacos_preciosos.preciososLacos.model.Pedido
import com.lacos_preciosos.preciososLacos.repository.*
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import java.util.*

class PedidoServiceTest {

    private val pedidoRepository = mock(PedidoRepository::class.java)
    private val usuarioRepository = mock(UsuarioRepository::class.java)
    private val produtoRepository = mock(ProdutoRepository::class.java)
    private val statusPagamentoRepository = mock(StatusPagamentoRepository::class.java)
    private val statusPedidoRepository = mock(StatusPedidoRepository::class.java)

    private val dto = CadastroPedidoDTO(10.0, "credito",1 , mutableListOf(1,2))
    private lateinit var service: PedidoService

    @BeforeEach
    fun setup() {
        service = PedidoService(
            pedidoRepository,
            usuarioRepository,
            produtoRepository,
            statusPagamentoRepository,
            statusPedidoRepository
        )
    }

    @Test
    @DisplayName("A consulta de todos os pedidos com dados deve retornar a lista completa")
    fun testGetAllPedidosComDados() {
        val pedido = Pedido(dto)

        `when`(pedidoRepository.findAll()).thenReturn(listOf(pedido))

        val resultado = service.getAllPedidos()

        assertEquals(1, resultado.size)
        assertEquals(10.0, resultado[0].total)
    }

    @Test
    @DisplayName("A consulta de todos os pedidos sem dados deve lançar exceção")
    fun testGetAllPedidosSemDados() {
        `when`(pedidoRepository.findAll()).thenReturn(emptyList())

        assertThrows(RuntimeException::class.java) {
            service.getAllPedidos()
        }
    }

    @Test
    @DisplayName("Buscar pedido por ID existente deve retornar os dados corretamente")
    fun testGetPedidoPorIdExistente() {

        `when`(pedidoRepository.findById(1)).thenReturn(Optional.of(Pedido(dto)))

        val resultado = service.getOnePedido(1)

        assertEquals(10.0, resultado.total)
        assertEquals("CREDITO", resultado.formaPagamento)

    }

    @Test
    @DisplayName("Buscar pedido por ID inexistente deve lançar exceção")
    fun testGetPedidoPorIdInexistente() {
        `when`(pedidoRepository.findById(999)).thenReturn(Optional.empty())

        assertThrows(RuntimeException::class.java) {
            service.getOnePedido(999)
        }
    }

    @Test
    @DisplayName("Excluir pedido existente deve chamar o delete do repositório")
    fun testDeletePedidoExistente() {
        `when`(pedidoRepository.existsById(1)).thenReturn(true)

        service.deletePedido(1)

        verify(pedidoRepository, times(1)).deleteById(1)
    }

    @Test
    @DisplayName("Excluir pedido inexistente deve lançar exceção de validação")
    fun testDeletePedidoInexistente() {
        `when`(pedidoRepository.existsById(999)).thenReturn(false)

        assertThrows(ValidacaoException::class.java) {
            service.deletePedido(999)
        }
    }
}
