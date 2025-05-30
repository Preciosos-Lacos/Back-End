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
    fun adicionarFavorito(dto: CadastroFavoritoDTO) {
//        val usuario = usuarioRepository.findById(dto.idUsuario)
//            .orElseThrow { RuntimeException("Usuário com esse ID não foi encontrado") }
//
//        val produto = produtoRepository.findById(dto.idModelo)
//            .orElseThrow { RuntimeException("Produto (Modelo) com esse ID não foi encontrado") }
//
//        val favoritoJaExiste = favoritoRepository.existsByUsuarioIdAndProdutoId(dto.idUsuario, dto.idModelo)
//
//        if (favoritoJaExiste) {
//            throw ValidacaoException("Esse produto já está nos favoritos do usuário")
//        }
//
//        val novoFavorito = Favorito(
//            idFavorito = dto.idFavorito
//        ).apply {
//            this.usuario = usuario
//            this.produto = produto
//        }

//        favoritoRepository.save(novoFavorito)
    }


    fun removerFavorito(idFavorito: Int) {
        val favorito = favoritoRepository.findById(idFavorito)
            .orElseThrow { ValidacaoException("Favorito com esse ID não foi encontrado") }

        favoritoRepository.delete(favorito)
    }
//
//    fun listarFavoritosDoUsuario(usuarioId: Int): List<DadosDetalheFavorito> {
//        if (!usuarioRepository.existsById(usuarioId)) {
//            throw RuntimeException("Usuário com esse ID não foi encontrado")
//        }
//
//        val favoritos = favoritoRepository.findAllByUsuarioId(usuarioId)
//
//        return favoritos.map { favorito ->
//            mapFavoritoToDto(favorito)
//        }
//    }

//    private fun mapFavoritoToDto(favorito: Favorito): DadosDetalheFavorito {
//        val produto = favorito.produto ?: throw RuntimeException("Favorito com produto nulo")
//        val usuario = favorito.usuario ?: throw RuntimeException("Favorito com usuário nulo")
//
//        return DadosDetalheFavorito(
//            idFavorito = favorito.idFavorito,
//            nome = produto.nome,
//            material = produto.material,
//            tamanho = produto.tamanho,
//            cor = produto.cor,
//            tipoLaco = produto.tipoLaco,
//            acabamento = produto.acabamento,
//            preco = produto.preco,
//            idUsuario = usuario.idUsuario
//        )
//    }
    
}
