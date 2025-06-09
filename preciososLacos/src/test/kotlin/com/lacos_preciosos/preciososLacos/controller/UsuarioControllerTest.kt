import com.lacos_preciosos.preciososLacos.controller.UsuarioController
import com.lacos_preciosos.preciososLacos.dto.usuario.CadastroUsuarioDTO
import com.lacos_preciosos.preciososLacos.model.Usuario
import com.lacos_preciosos.preciososLacos.service.UsuarioService
import com.lacos_preciosos.preciososLacos.repository.UsuarioRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.ResponseEntity
import java.time.LocalDate
import java.util.*

class UsuarioControllerTest {

    private lateinit var repository: UsuarioRepository
    private lateinit var service: UsuarioService
    private lateinit var controller: UsuarioController
    private lateinit var mockUsuario: Usuario

    @BeforeEach
    fun setup() {
        mockUsuario = Usuario(
            idUsuario = 1,
            nomeCompleto = "Marianna Serra",
            senha = "marianna123",
            telefone = "11999999999"
        )

        repository = mock(UsuarioRepository::class.java)
        service = UsuarioService(repository)
        controller = UsuarioController(service)

        `when`(repository.existsById(1)).thenReturn(true)
        `when`(repository.findById(1)).thenReturn(Optional.of(mockUsuario))
    }

    @Test
    @DisplayName("Cadastro de usuário com dados válidos deve retornar status 201")
    fun cadastrarUsuario() {
        val novoUsuario = CadastroUsuarioDTO(
            nomeCompleto = "Marianna Serra",
            senha = "marianna123",
            telefone = "11999999999",
            email = "marianna@gmail.com",
            cpf = "12345678901",
            data_cadastro = LocalDate.parse("1990-01-01")

        )

        `when`(repository.save(any(Usuario::class.java))).thenReturn(mockUsuario)

        val response = controller.cadastrarUsuario(novoUsuario)

        assertEquals(201, response.statusCode.value())
        assertEquals(mockUsuario.nomeCompleto, response.body?.nomeCompleto)
    }

    @Test
    @DisplayName("Listagem de usuários deve retornar status 200 e lista não vazia")
    fun listarUsuarios() {
        `when`(repository.findAll()).thenReturn(listOf(mockUsuario))

        val response: ResponseEntity<List<Usuario>> = controller.listarUsuarios()

        assertEquals(200, response.statusCode.value())
        assertTrue(response.body!!.isNotEmpty())
    }

    @Test
    @DisplayName("Listagem de usuários sem dados deve retornar status 204")
    fun listarUsuarioSemDados() {
        `when`(repository.findAll()).thenReturn(emptyList())

        val response = controller.listarUsuarios()

        assertEquals(204, response.statusCode.value())
        assertTrue(response.body!!.isEmpty())
    }

    @Test
    @DisplayName("A consulta por nome com dados deve retornar status 200 com a lista correta")
    fun listarPorNome() {
        val nomeTeste = "Marianna Serra"
        val usuario1 = Usuario(nomeCompleto = nomeTeste)
        val usuario2 = Usuario(nomeCompleto = nomeTeste)

        `when`(repository.findByNomeCompletoContains(nomeTeste)).thenReturn(listOf(usuario1, usuario2))

        val response = controller.listarPorNome(nomeTeste)

        assertEquals(200, response.statusCode.value())
        assertEquals(2, response.body!!.size)
        assertEquals(2, response.body?.count { it.nomeCompleto!!.contains(nomeTeste) })
    }

    @Test
    @DisplayName("A exclusão de usuário existente deve retornar status 204")
    fun deletarUsuarioExistente() {
        doNothing().`when`(repository).deleteById(1)

        val response = controller.deletarUsuario(1)

        assertEquals(204, response.statusCode.value())
        assertNull(response.body)
        verify(repository, times(1)).deleteById(1)
    }

    @Test
    @DisplayName("A exclusão de usuário inexistente deve retornar status 404")
    fun deletarUsuarioInexistente() {
        `when`(repository.existsById(999)).thenReturn(false)

        val response = controller.deletarUsuario(999)

        assertEquals(404, response.statusCode.value())
        assertNull(response.body)
        verify(repository, times(0)).deleteById(999)
    }
}