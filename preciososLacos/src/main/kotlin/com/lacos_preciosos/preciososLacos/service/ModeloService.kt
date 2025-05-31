package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.modelo.*
import com.lacos_preciosos.preciososLacos.dto.pedido.CadastroPedidoDTO
import com.lacos_preciosos.preciososLacos.dto.pedido.DadosDetalhePedido
import com.lacos_preciosos.preciososLacos.model.Modelo
import com.lacos_preciosos.preciososLacos.repository.ModeloRepository
import com.lacos_preciosos.preciososLacos.repository.UsuarioRepository
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.springframework.stereotype.Service
import java.util.*

@Service
class ModeloService(private val modeloRepository: ModeloRepository, val usuarioRepository: UsuarioRepository) {

    fun getAllModelos(): List<Modelo> {
        return modeloRepository.findAll()
    }

    fun getOneModelo(id: Int): Optional<Modelo> {
        return modeloRepository.findById(id)
    }

    fun getModeloByNome(nome: String): Optional<Modelo> {
        return modeloRepository.findByNomeModeloContainingIgnoreCase(nome)
    }

    fun createModelo(modeloDTO: CadastroModeloDTO): DadosDetalheModelo {
        val modelo = Modelo(modeloDTO)
        modeloRepository.save(modelo)
        return DadosDetalheModelo(modelo)
    }

    fun updateModelo(id: Int, modeloDto: CadastroModeloDTO): DadosDetalheModelo {

        var existe = modeloRepository.existsById(id)

        if (existe) {
            var modelo = Modelo(modeloDto)
            modelo.idModelo = id
            modeloRepository.save(modelo)
            return DadosDetalheModelo(modelo)
        } else {
            throw ValidacaoException("Modelo não encontrado")
        }
    }

//    fun updateFoto(id: Int, fotoDTO: AtualizacaoFotoDTO): DadosDetalheModelo {
//        var existe = modeloRepository.findById(id)
//
//        if (existe.isEmpty) {
//            throw ValidacaoException("Modelo não encontrado")
//        } else {
//            val modelo = existe.get()
//            modeloRepository.updateFoto(id, fotoDTO.foto)
//            modelo.foto = fotoDTO.foto
//            return DadosDetalheModelo(modelo)
//        }
//    }

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
}
