package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.dto.favorito.CadastroFavoritoDTO
import com.lacos_preciosos.preciososLacos.dto.favorito.DadosDetalheFavorito
import com.lacos_preciosos.preciososLacos.service.FavoritoService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/favoritos")
class FavoritoController(private val favoritoService: FavoritoService) {
    @PostMapping("/{usuarioId}/{produtoId}")
    @Tag(name = "Adicionar Favorito")
    fun adicionarFavorito(
        @RequestBody dto: CadastroFavoritoDTO
    ): ResponseEntity<Void> {
        return try {
            favoritoService.adicionarFavorito(dto)
            ResponseEntity.status(201).build()
        } catch (e: Exception) {
            ResponseEntity.status(400).build()
        }
    }

    @DeleteMapping("/{idFavorito}")
    @Tag(name = "Remover Favorito")
    fun removerFavorito(
        @PathVariable idFavorito: Int
    ): ResponseEntity<Void> {
        return try {
            favoritoService.removerFavorito(idFavorito)
            ResponseEntity.noContent().build()
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }
//
//    @GetMapping("/{usuarioId}")
//    @Tag(name = "Listar Favoritos do Usuário")
//    fun listarFavoritos(@PathVariable usuarioId: Int): ResponseEntity<List<DadosDetalheFavorito>> {
//        return try {
//            val favoritos = favoritoService.listarFavoritosDoUsuario(usuarioId)
//            ResponseEntity.ok(favoritos)
//        } catch (e: Exception) {
//            ResponseEntity.status(404).build()
//        }
//    }
}
