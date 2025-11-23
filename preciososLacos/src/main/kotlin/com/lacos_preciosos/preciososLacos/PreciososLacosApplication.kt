package com.lacos_preciosos.preciososLacos

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@OpenAPIDefinition(
    info = io.swagger.v3.oas.annotations.info.Info(
        title = "Preciosos Lacos API",
        version = "1.0"
    ), tags = [
        io.swagger.v3.oas.annotations.tags.Tag(name = "Cadastro de usuário"),
        io.swagger.v3.oas.annotations.tags.Tag(name = "Listagem de usuário"),
        io.swagger.v3.oas.annotations.tags.Tag(name = "Pesquisar usuário"),
        io.swagger.v3.oas.annotations.tags.Tag(name = "Atualização de usuário"),
        io.swagger.v3.oas.annotations.tags.Tag(name = "Exclusão de usuário"),
        io.swagger.v3.oas.annotations.tags.Tag(name = "Login de usuário"),
        io.swagger.v3.oas.annotations.tags.Tag(name = "Logoff de usuário"),
        io.swagger.v3.oas.annotations.tags.Tag(name = "Cadastro de Modelo"),
        io.swagger.v3.oas.annotations.tags.Tag(name = "Listagem de modelo"),
        io.swagger.v3.oas.annotations.tags.Tag(name = "Pesquisar modelo"),
        io.swagger.v3.oas.annotations.tags.Tag(name = "Atualização de modelo"),
        io.swagger.v3.oas.annotations.tags.Tag(name = "Atualização de foto"),
        io.swagger.v3.oas.annotations.tags.Tag(name = "Exclusão de modelo")
    ]
)


@SpringBootApplication
class PreciososLacosApplication

fun main(args: Array<String>) {
    runApplication<PreciososLacosApplication>(*args)
}
