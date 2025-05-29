package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.favorito.DadosDetalheFavorito
import com.lacos_preciosos.preciososLacos.dto.favorito.CadastroFavoritoDTO
import com.lacos_preciosos.preciososLacos.model.Favorito
import com.lacos_preciosos.preciososLacos.repository.FavoritoRepository
import com.lacos_preciosos.preciososLacos.repository.ProdutoRepository
import com.lacos_preciosos.preciososLacos.repository.UsuarioRepository
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.springframework.stereotype.Service

@Service
class FavoritoService(
    val favoritoRepository: FavoritoRepository,
    val usuarioRepository: UsuarioRepository,
    val produtoRepository: ProdutoRepository
) {

    fun createFavorito(dto: CadastroFavoritoDTO): DadosDetalheFavorito {
        val usuario = usuarioRepository.findById(dto.idUsuario)
            .orElseThrow { RuntimeException("Usuário com esse ID não foi encontrado") }

        val produto = produtoRepository.findById(dto.idModelo)
            .orElseThrow { RuntimeException("Produto (Modelo) com esse ID não foi encontrado") }

        // Verifica se já existe um favorito com o mesmo idFavorito
        if (favoritoRepository.existsById(dto.idFavorito)) {
            throw ValidacaoException("Favorito com esse ID já existe")
        }

        val favorito = Favorito(dto).apply {
            this.usuario = usuario
            this.produto = produto
        }

        favoritoRepository.save(favorito)

        return mapFavoritoToDto(favorito)
    }

    fun getAllFavoritosDoUsuario(idUsuario: Int): List<DadosDetalheFavorito> {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw RuntimeException("Usuário com esse ID não foi encontrado")
        }

        val favoritos = favoritoRepository.findAllByUsuarioId(idUsuario)

        if (favoritos.isEmpty()) {
            throw RuntimeException("Nenhum favorito encontrado para o usuário")
        }

        return favoritos.map { mapFavoritoToDto(it) }
    }

    fun getFavoritoById(idFavorito: Int): DadosDetalheFavorito {
        val favorito = favoritoRepository.findById(idFavorito)
            .orElseThrow { RuntimeException("Favorito com esse ID não foi encontrado") }

        return mapFavoritoToDto(favorito)
    }

    fun updateFavorito(idFavorito: Int, dto: CadastroFavoritoDTO): DadosDetalheFavorito {
        val favoritoExistente = favoritoRepository.findById(idFavorito)
            .orElseThrow { RuntimeException("Favorito com esse ID não foi encontrado") }

        val usuario = usuarioRepository.findById(dto.idUsuario)
            .orElseThrow { RuntimeException("Usuário com esse ID não foi encontrado") }

        val produto = produtoRepository.findById(dto.idModelo)
            .orElseThrow { RuntimeException("Produto (Modelo) com esse ID não foi encontrado") }

        val favoritoAtualizado = Favorito(dto).apply {
            this.idFavorito = idFavorito
            this.usuario = usuario
            this.produto = produto
        }

        favoritoRepository.save(favoritoAtualizado)

        return mapFavoritoToDto(favoritoAtualizado)
    }

    fun deleteFavorito(idFavorito: Int) {
        if (!favoritoRepository.existsById(idFavorito)) {
            throw ValidacaoException("Favorito não encontrado")
        }

        favoritoRepository.deleteById(idFavorito)
    }

    private fun mapFavoritoToDto(favorito: Favorito): DadosDetalheFavorito {
        val produto = favorito.produto ?: throw RuntimeException("Favorito com produto nulo")
        val usuario = favorito.usuario ?: throw RuntimeException("Favorito com usuário nulo")

        return DadosDetalheFavorito(
            idFavorito = favorito.idFavorito,
            nome = produto.nome,
            material = produto.material,
            tamanho = produto.tamanho,
            cor = produto.cor,
            tipoLaco = produto.tipoLaco,
            acabamento = produto.acabamento,
            preco = produto.preco,
            idModelo = produto.idModelo,
            idUsuario = usuario.idUsuario
        )
    }
}
