
package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.dto.imagem.ImagemDTO
import com.lacos_preciosos.preciososLacos.dto.modelo.*
import com.lacos_preciosos.preciososLacos.model.Modelo
import com.lacos_preciosos.preciososLacos.service.ModeloService
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/modelos")
class ModeloController(private val modeloService: ModeloService) {

    @GetMapping("/catalogo/maisVendidos")
    @Tag(name = "Catálogo de modelos mais vendidos")
    fun modelosCatalogoMaisVendidos(): ResponseEntity<List<ModeloCatalogoDTO>> {
        val lista = modeloService.modelosCatalogoMaisVendidos()
        return if (lista.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(lista)
        }
    }

    @GetMapping("/catalogo")
    @Tag(name = "Catálogo de modelos")
    fun modelosCatalogo(): ResponseEntity<List<ModeloCatalogoDTO>> {
        val lista = modeloService.modelosCatalogo()
        return if (lista.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(lista)
        }
    }
    @PostMapping
    @Tag(name = "Cadastro de Modelo")
    fun createModelo(
        @RequestBody @Valid criacaoModeloDTO: CadastroModeloDTO,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<DadosDetalheModelo> {
        val modelo = modeloService.createModelo(criacaoModeloDTO)
        val uri = uriBuilder.path("/modelos/{id}").buildAndExpand(modelo.idModelo).toUri()
        return ResponseEntity.created(uri).body(modelo)
    }

    @GetMapping
    @Tag(name = "Listagem de modelo")
    fun getAllModelos(): ResponseEntity<List<DadosDetalheModelo>> {
        val modelos = modeloService.getAllModelos()
        return if (modelos.isEmpty()) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.ok(modelos)
        }
    }


    @GetMapping("/{id}")
    fun getOneModelo(@PathVariable id: Int): ResponseEntity<Modelo> {
        return ResponseEntity.of(modeloService.getOneModelo(id))
    }

    @GetMapping("/pesquisa-nome")
    @Tag(name = "Pesquisar modelo")
    fun getModeloByName(@RequestParam nome: String): ResponseEntity<Modelo> {
        val modelo = modeloService.getModeloByNome(nome)

        return ResponseEntity.of(modelo)
    }

    @PutMapping("/{id}")
    @Tag(name = "Atualização de modelo")
    fun updateModelo(
        @PathVariable id: Int,
        @RequestBody @Valid criacaoModeloDTO: CadastroModeloDTO
    ): ResponseEntity<DadosDetalheModelo> {

        try {
            return ResponseEntity.status(200).body(modeloService.updateModelo(id, criacaoModeloDTO))
        } catch (ex: ValidacaoException) {
            return ResponseEntity.status(404).build()
        }
    }

    @PatchMapping("/{id}/foto")
    @Tag(name = "Atualização de foto")
    fun updateFoto(
        @PathVariable id: Int,
        @RequestBody @Valid imagemDTO: ImagemDTO
    ): ResponseEntity<DadosDetalheModelo> {
        try {
            val modelo = modeloService.getOneModelo(id).orElseThrow { ValidacaoException("Modelo não encontrado") }
            modelo.adicionarFoto(imagemDTO.imagemBase64)
            modeloService.updateFoto(id, imagemDTO.imagemBase64)
            return ResponseEntity.ok(DadosDetalheModelo(modelo))
        } catch (ex: ValidacaoException) {
            return ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    @Tag(name = "Exclusão de modelo")
    fun deleteModelo(@PathVariable id: Int): ResponseEntity<Void> {
        try {
            modeloService.deleteModelo(id)
            return ResponseEntity.status(204).build()
        } catch (ex: ValidacaoException) {
            return ResponseEntity.status(404).build()
        }
    }

    @PostMapping("/favorito")
    @Tag(name = "Adicionando favorito")
    fun adicionarFavorito(@RequestBody dto: CadastroFavoritoDTO): ResponseEntity<DadosDetalheModelo> {
        try {
            var modelo = modeloService.adicionarFavorito(dto)
            return ResponseEntity.status(201).body(modelo)
        } catch (e: RuntimeException) {
            return ResponseEntity.status(404).build()
        }
    }

    @DeleteMapping("/favorito")
    @Tag(name = "Exclusão de favorito")
    fun deleteFavorito(@RequestBody dto: DeleteFavoritoDTO): ResponseEntity<Void> {
        try {
            modeloService.deleteFavorito(dto)
            return ResponseEntity.status(204).build()
        } catch (ex: ValidacaoException) {
            return ResponseEntity.status(404).build()
        }
    }

    @GetMapping("/{id}/foto")
    @Tag(name = "Obter foto do modelo")
    fun getFotoModelo(@PathVariable id: Int): ResponseEntity<Map<String, String>> {
        try {
            val fotoBase64 = modeloService.getFotoModelo(id)

            if (fotoBase64 == null) {
                return ResponseEntity.noContent().build()
            }

            val response = mapOf("foto" to fotoBase64)
            return ResponseEntity.ok(response)
        } catch (ex: ValidacaoException) {
            return ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/favoritos/{idUsuario}")
    @Tag(name = "Favoritos do usuário")
    fun getFavoritosByUsuario(@PathVariable idUsuario: Int): ResponseEntity<List<Modelo>> {
        val favoritos = modeloService.getFavoritosByUsuario(idUsuario)
        return if (favoritos.isEmpty()) ResponseEntity.noContent().build() else ResponseEntity.ok(favoritos)
    }
}