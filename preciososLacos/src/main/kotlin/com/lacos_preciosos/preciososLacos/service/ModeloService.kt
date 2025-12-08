package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.modelo.*
import com.lacos_preciosos.preciososLacos.model.Modelo
import com.lacos_preciosos.preciososLacos.repository.ModeloRepository
import com.lacos_preciosos.preciososLacos.repository.UsuarioRepository
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.springframework.stereotype.Service
import java.util.*

@Service
class ModeloService(private val modeloRepository: ModeloRepository, val usuarioRepository: UsuarioRepository) {

    // Função para catálogo dos mais vendidos ou aleatórios
    fun modelosCatalogoMaisVendidos(): List<ModeloCatalogoDTO> {
        val maisVendidos = modeloRepository.findMaisVendidos()
        val modelos = if (maisVendidos.isNotEmpty()) maisVendidos else modeloRepository.findAleatorios()
        return modelos.map { modelo ->
            ModeloCatalogoDTO(
                idModelo = modelo.idModelo,
                nome = modelo.nomeModelo,
                preco = modelo.preco,
                colecao = modelo.nomeModelo, // ajuste se necessário
                fotoModelo = modelo.getFotoBase64()
            )
        }
    }

    // Função para catálogo: retorna imagem base64, nome, preco, colecao
    fun modelosCatalogo(): List<ModeloCatalogoDTO> {
        return modeloRepository.findAll().map { modelo ->
            ModeloCatalogoDTO(
                idModelo = modelo.idModelo,
                nome = modelo.nomeModelo,
                preco = modelo.preco,
                colecao = modelo.nomeModelo, // Se colecao for outro campo, ajuste aqui
                fotoModelo = modelo.getFotoBase64()
            )
        }
    }

    fun getAllModelos(): List<DadosDetalheModelo> {
        return modeloRepository.findAll().map { DadosDetalheModelo(it) }
    }

    fun getOneModelo(id: Int): Optional<Modelo> {
        return modeloRepository.findById(id)
    }

    fun getModeloByNome(nome: String): Optional<Modelo> {
        return modeloRepository.findByNomeModeloContainingIgnoreCase(nome)
    }

    fun createModelo(modeloDTO: CadastroModeloDTO): DadosDetalheModelo {
        val modelo = Modelo(modeloDTO, 1, "Laço Simples", 10.0, "Descrição")
        modeloRepository.save(modelo)
        return DadosDetalheModelo(modelo)
    }

    fun updateModelo(id: Int, modeloDto: CadastroModeloDTO): DadosDetalheModelo {

        var existe = modeloRepository.existsById(id)

        if (existe) {
            var modelo = Modelo(modeloDto, 1, "Laço Simples", 10.0, "Descrição")
            modelo.idModelo = id
            modeloRepository.save(modelo)
            return DadosDetalheModelo(modelo)
        } else {
            throw ValidacaoException("Modelo não encontrado")
        }
    }

    fun updateFoto(id: Int, foto: String): DadosDetalheModelo {
        var existe = modeloRepository.findById(id)

        if (existe.isEmpty) {
            throw ValidacaoException("Modelo não encontrado")
        } else {
            val modelo = existe.get()
            // Converter a String Base64 para ByteArray antes de chamar o repositório
            val fotoBytes = java.util.Base64.getDecoder().decode(foto)
            modeloRepository.updateFoto(id, fotoBytes)
            return DadosDetalheModelo(modelo)
        }
    }

    fun deleteModelo(id: Int) {
        var existe = modeloRepository.existsById(id)

        if (existe) {
            modeloRepository.deleteById(id)
        } else {
            throw ValidacaoException("Modelo não encontrado")
        }
    }

    fun adicionarFavorito(dto: CadastroFavoritoDTO):DadosDetalheModelo{
        var usuarioOptional = usuarioRepository.findById(dto.idUsuario)

        if (usuarioOptional.isEmpty) {
            throw RuntimeException("Usuário com esse ID não foi encontrado")
        }

        if (modeloRepository.existsById(dto.idModelo) == false){
            throw RuntimeException("Modelo com esse ID não foi encontrado")
        }

        modeloRepository.adicionarFavorito(dto.idModelo,dto.idUsuario)

        return DadosDetalheModelo(modeloRepository.findById(dto.idModelo).get())
    }

    fun deleteFavorito(dto: DeleteFavoritoDTO){
        var usuarioOptional = usuarioRepository.findById(dto.idUsuario)

        if (usuarioOptional.isEmpty) {
            throw RuntimeException("Usuário com esse ID não foi encontrado")
        }

        if (modeloRepository.existsById(dto.idModelo) == false){
            throw RuntimeException("Modelo com esse ID não foi encontrado")
        }

        modeloRepository.deleteFavorito(dto.idModelo,dto.idUsuario)
    }

    // Método para obter apenas a foto do modelo como Base64
    fun getFotoModelo(id: Int): String? {
        val modeloOptional = modeloRepository.findById(id)
        
        if (modeloOptional.isEmpty) {
            throw ValidacaoException("Modelo não encontrado")
        }
        
        val modelo = modeloOptional.get()
        return modelo.getFotoBase64()
    }

    fun getFavoritosByUsuario(idUsuario: Int): List<Modelo> {
        return modeloRepository.findFavoritosByUsuario(idUsuario)
    }

    // Novo método para atualizar o campo "ativo"
    fun setAtivo(idModelo: Int, ativo: Boolean): Modelo {
        val existe = modeloRepository.existsById(idModelo)
        if (!existe) {
            throw ValidacaoException("Modelo não encontrado")
        }

        modeloRepository.updateAtivo(idModelo, ativo)
        return modeloRepository.findById(idModelo).orElseThrow { ValidacaoException("Modelo não encontrado") }
    }
}
