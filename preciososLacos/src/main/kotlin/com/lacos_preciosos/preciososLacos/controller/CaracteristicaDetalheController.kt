package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.model.CaracteristicaDetalhe
import com.lacos_preciosos.preciososLacos.repository.CaracteristicaDetalheRepository
import com.lacos_preciosos.preciososLacos.repository.CaracteristicaRepository
import com.lacos_preciosos.preciososLacos.service.CaracteristicaDetalheService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("caracteristica-detalhe")
class CaracteristicaDetalheController(val repository: CaracteristicaDetalheRepository) {

    @PostMapping
    fun createCaracteristicaDetalhe(
        @RequestBody caracteristicaDetalhe: CaracteristicaDetalhe,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<CaracteristicaDetalhe> {
        val caracteristica = repository.create(caracteristicaDetalhe)
        return ResponseEntity.status(201).body(caracteristica)
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<CaracteristicaDetalhe>> {
        val detalhes = repository.getAll()
        return ResponseEntity.status(200).body(detalhes)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Int,
        @RequestBody updated: CaracteristicaDetalhe
    ): ResponseEntity<CaracteristicaDetalhe> {
        val detalhe = repository.update(id, updated)
        return ResponseEntity.ok(detalhe)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {
        repository.delete(id)
        return ResponseEntity.status(404).build()
    }
}