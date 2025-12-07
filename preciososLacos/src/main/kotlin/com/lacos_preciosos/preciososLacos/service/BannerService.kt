package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.banner.BannerDTO
import com.lacos_preciosos.preciososLacos.model.Banner
import com.lacos_preciosos.preciososLacos.repository.BannerRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class BannerService(val bannerRepository: BannerRepository) {

    fun listarTodos(ativo: Boolean?, data: Date?): List<BannerDTO> {
        return bannerRepository.findByFiltro(ativo, data).map { BannerDTO(it) }
    }

    fun buscarPorId(id: Long): BannerDTO? {
        return bannerRepository.findById(id).map { BannerDTO(it) }.orElse(null)
    }

    fun criarBanner(dto: BannerDTO): BannerDTO {
        // Desativa todos os outros banners
        bannerRepository.desativarTodos()
        // Define a ordem automaticamente: maior ordem + 1
        val maiorOrdem = bannerRepository.findAll().maxOfOrNull { it.ordem } ?: 0
        val novaOrdem = maiorOrdem + 1
        val banner = Banner(
            titulo = dto.titulo,
            imagemUrl = dto.imagemUrl,
            linkDestino = dto.linkDestino,
            ordem = novaOrdem,
            ativo = true, // Sempre ativo ao criar
            dataInicio = dto.dataInicio,
            dataFim = dto.dataFim
        )
        return BannerDTO(bannerRepository.save(banner))
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
        return BannerDTO(bannerRepository.save(banner))
    }

    fun removerBanner(id: Long) {
        bannerRepository.deleteById(id)
    }

    fun ativarBanner(id: Long, ativo: Boolean): BannerDTO? {
        if (ativo) {
            bannerRepository.desativarTodos()
        }
        val banner = bannerRepository.findById(id).orElse(null) ?: return null
        banner.ativo = ativo
        banner.updatedAt = java.time.LocalDateTime.now()
        return BannerDTO(bannerRepository.save(banner))
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
