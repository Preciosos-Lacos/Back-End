package com.lacos_preciosos.preciososLacos.controller

import com.lacos_preciosos.preciososLacos.dto.banner.BannerDTO
import com.lacos_preciosos.preciososLacos.service.BannerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/banners")
class BannerController(val bannerService: BannerService) {

    @GetMapping
    fun listarBanners(
        @RequestParam(required = false) ativo: Boolean?,
        @RequestParam(required = false) data: Date?
    ): ResponseEntity<List<BannerDTO>> =
        ResponseEntity.ok(bannerService.listarTodos(ativo, data))

    @GetMapping("/{id}")
    fun buscarBanner(@PathVariable id: Long): ResponseEntity<BannerDTO> {
        val banner = bannerService.buscarPorId(id)
        return if (banner != null) ResponseEntity.ok(banner) else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun criarBanner(@RequestBody dto: BannerDTO): ResponseEntity<BannerDTO> =
        ResponseEntity.status(201).body(bannerService.criarBanner(dto))

    @PutMapping("/{id}")
    fun atualizarBanner(@PathVariable id: Long, @RequestBody dto: BannerDTO): ResponseEntity<BannerDTO> {
        val banner = bannerService.atualizarBanner(id, dto)
        return if (banner != null) ResponseEntity.ok(banner) else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun removerBanner(@PathVariable id: Long): ResponseEntity<Void> {
        bannerService.removerBanner(id)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}/ativo")
    fun ativarBanner(@PathVariable id: Long, @RequestParam ativo: Boolean): ResponseEntity<BannerDTO> {
        val banner = bannerService.ativarBanner(id, ativo)
        return if (banner != null) ResponseEntity.ok(banner) else ResponseEntity.notFound().build()
    }

    @PostMapping("/upload")
    fun uploadBannerImage(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("titulo") titulo: String
    ): ResponseEntity<Map<String, String>> {
        if (file.isEmpty) {
            return ResponseEntity.badRequest().body(mapOf("error" to "Arquivo vazio"))
        }
        // Normaliza o título para nome de arquivo seguro
        val normalizedTitle = titulo
            .lowercase()
            .replace("[áàâãä]".toRegex(), "a")
            .replace("[éèêë]".toRegex(), "e")
            .replace("[íìîï]".toRegex(), "i")
            .replace("[óòôõö]".toRegex(), "o")
            .replace("[úùûü]".toRegex(), "u")
            .replace("ç", "c")
            .replace("[^a-z0-9]".toRegex(), "_")
            .replace("_+", "_")
            .trim('_')

        val extension = file.originalFilename?.substringAfterLast('.', "jpg")
        val filename = "${normalizedTitle}_${System.currentTimeMillis()}.$extension"
        val uploadDir = java.nio.file.Paths.get("uploads")
        if (!java.nio.file.Files.exists(uploadDir)) java.nio.file.Files.createDirectories(uploadDir)
        val filePath = uploadDir.resolve(filename)
        java.nio.file.Files.copy(file.inputStream, filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING)
        val url = "/uploads/$filename" // ajuste conforme sua rota de arquivos estáticos
        return ResponseEntity.ok(mapOf("imagemUrl" to url, "nomeArquivo" to filename))
    }

    @GetMapping("/ativo/home")
    fun bannerAtivoHome(): ResponseEntity<BannerDTO> {
        val banner = bannerService.buscarBannerAtivoParaHome()
        return if (banner != null) ResponseEntity.ok(banner) else ResponseEntity.notFound().build()
    }
}
