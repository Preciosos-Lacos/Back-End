package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.dto.cor.CadastroCorDTO
import com.lacos_preciosos.preciososLacos.dto.imagem.ImagemDTO
import com.lacos_preciosos.preciososLacos.model.CaracteristicaDetalhe
import com.lacos_preciosos.preciososLacos.repository.CaracteristicaDetalheRepository
import com.lacos_preciosos.preciososLacos.repository.CaracteristicaRepository
import com.lacos_preciosos.preciososLacos.service.CaracteristicaDetalheService
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("caracteristica-detalhe")
class CaracteristicaDetalheController(private val caracteristicaDetalheService: CaracteristicaDetalheService) {

    @PostMapping
    fun createCaracteristicaDetalhe(@RequestBody caracteristicaDetalhe: CaracteristicaDetalhe, uriBuilder: UriComponentsBuilder
    ):
            ResponseEntity<CaracteristicaDetalhe> {
        val caracteristica = caracteristicaDetalheService.create(caracteristicaDetalhe)
        return ResponseEntity.status(201).body(caracteristica)
    }

    @PostMapping("/cor")
    fun cadastroCor(@RequestBody cadastroCorDTO: CadastroCorDTO): ResponseEntity<String> {
        try {
            return ResponseEntity.ok(caracteristicaDetalheService.saveCor(cadastroCorDTO));
        } catch(ex : ValidacaoException){
            return ResponseEntity.status(404).build()
        }
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<CaracteristicaDetalhe>> {

        val caracteristicas = caracteristicaDetalheService.getAllCaracteristicas()

        return if (caracteristicas.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(caracteristicas)
        }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody updated: CaracteristicaDetalhe):
            ResponseEntity<CaracteristicaDetalhe> {
        try {
            val detalhe = caracteristicaDetalheService.update(id, updated)
            return ResponseEntity.ok(detalhe)
        } catch (ex: ValidacaoException) {
            return ResponseEntity.status(404).build()
        }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {
        try {
            caracteristicaDetalheService.delete(id)
            return ResponseEntity.status(404).build()
        } catch (ex: ValidacaoException) {
            return ResponseEntity.status(404).build()
        }
    }

    @PatchMapping("/{id}/imagem")
    @Tag(name = "Atualização de imagem") fun updateImagem(@PathVariable id: Int, @RequestBody @Valid imagemDTO: ImagemDTO):
            ResponseEntity<CaracteristicaDetalhe> {
        try {
            val detalhe = caracteristicaDetalheService.subirImagem(id, java.util.Base64.getDecoder().decode(imagemDTO.imagemBase64))
            return ResponseEntity.ok(detalhe)
        } catch (ex: Exception) {
            return ResponseEntity.notFound().build()
        }
    }
}