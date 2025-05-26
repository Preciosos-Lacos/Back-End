package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.model.CaracteristicaDetalhe
import com.lacos_preciosos.preciososLacos.service.CaracteristicaDetalheService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("caracteristica-detalhe")
class CaracteristicaDetalheController(
    private val caracteristicaDetalheService: CaracteristicaDetalheService
) {

    @PostMapping
    fun createCaracteristicaDetalhe(
        @RequestBody caracteristicaDetalhe: CaracteristicaDetalhe,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<CaracteristicaDetalhe> {
        val saved = caracteristicaDetalheService.create(caracteristicaDetalhe)
        val uri = uriBuilder.path("/caracteristica-detalhe/{id}")
            .buildAndExpand(saved.idCaracteristicaDetalhe).toUri()
        return ResponseEntity.created(uri).body(saved)
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<CaracteristicaDetalhe>> {
        val detalhes = caracteristicaDetalheService.getAll()
        return ResponseEntity.ok(detalhes)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Int,
        @RequestBody updated: CaracteristicaDetalhe
    ): ResponseEntity<CaracteristicaDetalhe> {
        val detalhe = caracteristicaDetalheService.update(id, updated)
        return ResponseEntity.ok(detalhe)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {
        caracteristicaDetalheService.delete(id)
        return ResponseEntity.noContent().build()
    }
}