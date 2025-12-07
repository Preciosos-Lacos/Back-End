package com.lacos_preciosos.preciososLacos.validacao

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors = ex.bindingResult.fieldErrors.map { fieldError ->
            fieldError.field to (fieldError.defaultMessage ?: "")
        }.toMap()
        val body = mapOf("erros" to errors)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(ValidacaoException::class)
    fun handleNotFound(ex: ValidacaoException): ResponseEntity<Void> {
        // For existing behavior, return 404 with no body
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }
}

