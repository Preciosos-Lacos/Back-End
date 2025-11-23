package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.banner.BannerDTO
import com.lacos_preciosos.preciososLacos.model.Banner
import com.lacos_preciosos.preciososLacos.repository.BannerRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class BannerService(val bannerRepository: BannerRepository) {
    // Ativa o banner pelo id e desativa todos os outros
    private fun ativarSomenteEsteBanner(id: Long) {
        val todos = bannerRepository.findAll()
        todos.forEach {
            it.ativo = (it.id == id)
            bannerRepository.save(it)
        }
    }

    fun listarTodos(ativo: Boolean?, data: Date?): List<BannerDTO> {
        return bannerRepository.findByFiltro(ativo, data).map { BannerDTO(it) }
    }

    fun buscarPorId(id: Long): BannerDTO? {
        return bannerRepository.findById(id).map { BannerDTO(it) }.orElse(null)
    }

    fun criarBanner(dto: BannerDTO): BannerDTO {
        // Define a ordem automaticamente: maior ordem + 1
        val maiorOrdem = bannerRepository.findAll().maxOfOrNull { it.ordem } ?: 0
        val novaOrdem = maiorOrdem + 1
        val banner = Banner(
            titulo = dto.titulo,
            imagemUrl = dto.imagemUrl,
            linkDestino = dto.linkDestino,
            ordem = novaOrdem,
            ativo = dto.ativo,
            dataInicio = dto.dataInicio,
            dataFim = dto.dataFim
        )
        val salvo = bannerRepository.save(banner)
        if (dto.ativo) {
            ativarSomenteEsteBanner(salvo.id!!)
        }
        return BannerDTO(salvo)
    }

    fun atualizarBanner(id: Long, dto: BannerDTO): BannerDTO? {
        val banner = bannerRepository.findById(id).orElse(null) ?: return null
        banner.titulo = dto.titulo
        banner.imagemUrl = dto.imagemUrl
        banner.linkDestino = dto.linkDestino
        // Não altera ordem pelo frontend
        banner.ativo = dto.ativo
        banner.dataInicio = dto.dataInicio
        banner.dataFim = dto.dataFim
        banner.updatedAt = java.time.LocalDateTime.now()
        val salvo = bannerRepository.save(banner)
        if (dto.ativo) {
            ativarSomenteEsteBanner(id)
        }
        return BannerDTO(salvo)
    }

    fun removerBanner(id: Long) {
        bannerRepository.deleteById(id)
    }

    fun ativarBanner(id: Long, ativo: Boolean): BannerDTO? {
        val banner = bannerRepository.findById(id).orElse(null) ?: return null
        if (ativo) {
            // Desativa todos os outros
            val todos = bannerRepository.findAll()
            todos.forEach {
                it.ativo = (it.id == id)
                bannerRepository.save(it)
            }
        } else {
            banner.ativo = false
            bannerRepository.save(banner)
        }
        banner.updatedAt = java.time.LocalDateTime.now()
        return BannerDTO(banner)
    }

    fun uploadImagem(file: MultipartFile): String {
        // Aqui você pode salvar no disco ou serviço externo e retornar a URL
        // Exemplo: salvar localmente
        val fileName = UUID.randomUUID().toString() + "_" + file.originalFilename
        val path = "uploads/banners/$fileName"
        file.inputStream.use { input ->
            java.io.File(path).outputStream().use { output ->
                input.copyTo(output)
            }
        }
        // Retorne a URL acessível pelo frontend
        return "/$path"
    }

    fun buscarBannerAtivoParaHome(): BannerDTO? {
        return bannerRepository.findByAtivoTrueOrderByOrdemAsc().firstOrNull()?.let { BannerDTO(it) }
    }
}
