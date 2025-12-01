package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.pedido.PedidoDTO
import com.lacos_preciosos.preciososLacos.dto.pedido.ItemPedidoDTO
import com.lacos_preciosos.preciososLacos.dto.pedido.ClienteResumoDTO
import com.lacos_preciosos.preciososLacos.dto.PedidoResumoDTO
import com.lacos_preciosos.preciososLacos.dto.pedido.CadastroPedidoDTO
import com.lacos_preciosos.preciososLacos.dto.pedido.DadosDetalhePedido
import com.lacos_preciosos.preciososLacos.dto.pedido.PedidoResumoCompletoDTO
import com.lacos_preciosos.preciososLacos.dto.produto.ProdutoDTO
import com.lacos_preciosos.preciososLacos.model.Pedido
import com.lacos_preciosos.preciososLacos.model.Produto
import com.lacos_preciosos.preciososLacos.repository.PedidoRepository
import com.lacos_preciosos.preciososLacos.repository.UsuarioRepository
import com.lacos_preciosos.preciososLacos.repository.ProdutoRepository
import com.lacos_preciosos.preciososLacos.repository.StatusPagamentoRepository
import com.lacos_preciosos.preciososLacos.repository.StatusPedidoRepository
import com.lacos_preciosos.preciososLacos.repository.CaracteristicaDetalheRepository
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import com.lacos_preciosos.preciososLacos.dto.pedido.ListaPedidosDTO
import com.lacos_preciosos.preciososLacos.dto.pedido.ItemPedidoDetalheDTO
import com.lacos_preciosos.preciososLacos.dto.pedido.CaracteristicaItemDTO
import org.springframework.security.core.context.SecurityContextHolder
import org.slf4j.Logger

@Service
class PedidoService(
    private val logger: Logger = LoggerFactory.getLogger(PedidoService::class.java),
    val pedidoRepository: PedidoRepository,
    val usuarioRepository: UsuarioRepository,
    val produtoRepository: ProdutoRepository,
    val statusPagamentoRepository: StatusPagamentoRepository,
    val statusPedidoRepository: StatusPedidoRepository,
    val caracteristicaDetalheRepository: CaracteristicaDetalheRepository
) {

    fun getUltimoPedidoUsuario(idUsuario: Int): PedidoDTO? {
        val pedido = pedidoRepository.findTopByUsuarioIdUsuarioOrderByIdPedidoDesc(idUsuario)
        return if (pedido != null) montarPedidoDTOBasico(pedido) else null
    }
    fun adicionarProdutoAoCarrinho(idUsuario: Int, idProduto: Int): PedidoDTO {
        val usuario = usuarioRepository.findById(idUsuario).orElseThrow { RuntimeException("Usuário não encontrado") }
        val produto = produtoRepository.findById(idProduto).orElseThrow { RuntimeException("Produto não encontrado") }
        val pedidoCarrinho = pedidoRepository.findFirstByUsuarioIdUsuarioAndCarrinhoTrue(idUsuario)
        val pedido = if (pedidoCarrinho != null) {
            val lista = (pedidoCarrinho.produtos ?: emptyList()) + produto
            pedidoCarrinho.produtos = lista
            pedidoCarrinho.total = lista.sumOf { it.preco ?: 0.0 }
            pedidoCarrinho
        } else {
            val novo = Pedido(
                idPedido = null,
                dataPedido = java.time.LocalDate.now(),
                total = produto.preco ?: 0.0,
                formaPagamento = null,
                usuario = usuario,
                statusPedido = null,
                statusPagamento = null,
                produtos = listOf(produto),
                carrinho = true
            )
            novo
        }
        pedidoRepository.save(pedido)
        return montarPedidoDTOBasico(pedido)
    }

    fun obterCarrinhoDoUsuario(idUsuario: Int): PedidoDTO? {
        val pedido = pedidoRepository.findFirstByUsuarioIdUsuarioAndCarrinhoTrue(idUsuario) ?: return null
        return montarPedidoDTOBasico(pedido)
    }

    fun removerProdutoDoCarrinho(idUsuario: Int, idProduto: Int): PedidoDTO? {
        val pedido = pedidoRepository.findFirstByUsuarioIdUsuarioAndCarrinhoTrue(idUsuario) ?: return null
        val listaAtual = pedido.produtos ?: emptyList()
        val novaLista = listaAtual.filterNot { it.idProduto == idProduto }
        pedido.produtos = novaLista
        pedido.total = novaLista.sumOf { it.preco ?: 0.0 }
        pedidoRepository.save(pedido)
        return obterCarrinhoDoUsuario(idUsuario)
    }

    // Remove apenas uma unidade do produto no carrinho (se existir múltiplas entradas)
    fun removerUmaUnidadeDoCarrinho(idUsuario: Int, idProduto: Int): PedidoDTO? {
        val pedido = pedidoRepository.findFirstByUsuarioIdUsuarioAndCarrinhoTrue(idUsuario) ?: return null
        val listaAtual = pedido.produtos ?: emptyList()
        val mutable = listaAtual.toMutableList()
        val index = mutable.indexOfFirst { it.idProduto == idProduto }
        if (index >= 0) {
            mutable.removeAt(index)
            pedido.produtos = mutable.toList()
            pedido.total = pedido.produtos?.sumOf { it.preco ?: 0.0 } ?: 0.0
            pedidoRepository.save(pedido)
        }
        return obterCarrinhoDoUsuario(idUsuario)
    }

    fun listarProdutosCarrinho(idUsuario: Int): List<ProdutoDTO> {
        val pedido = pedidoRepository.findFirstByUsuarioIdUsuarioAndCarrinhoTrue(idUsuario) ?: return emptyList()
        return pedido.produtos?.map { produto ->
            ProdutoDTO(
                idProduto = produto.idProduto,
                idModelo = produto.modelo?.idModelo,
                nome = produto.nome,
                colecao = produto.modelo?.nomeModelo,
                tamanho = produto.tamanho,
                tipoLaco = produto.tipoLaco,
                material = produto.material,
                cor = produto.cor,
                corDescricao = produto.cor,
                acabamento = produto.acabamento,
                acabamentoDescricao = produto.acabamento,
                foto = produto.modelo?.getFotoBase64(),
                preco = produto.preco
            )
        } ?: emptyList()
    }

    private fun montarPedidoDTOBasico(pedido: Pedido): PedidoDTO {
        return PedidoDTO(
            id = pedido.idPedido,
            numeroPedido = null,
            dataPedido = pedido.dataPedido.toString(),
            valorTotal = pedido.total,
            formaPagamento = pedido.formaPagamento?.name,
            statusPagamento = pedido.statusPagamento?.status,
            statusPedido = pedido.statusPedido?.status,
            cliente = ClienteResumoDTO(
                nome = pedido.usuario?.nomeCompleto,
                telefone = pedido.usuario?.telefone,
                email = pedido.usuario?.login
            ),
            enderecoEntrega = null,
            itens = pedido.produtos?.map { p ->
                ItemPedidoDTO(
                    sku = p.idProduto?.toString(),
                    nome = p.nome,
                    quantidade = 1,
                    preco = p.preco
                )
            } ?: emptyList(),
            modelos = null,
            tamanho = null,
            cores = null
        )
    }

    fun atualizarStatusPedido(id: Int, novoStatus: String): PedidoDTO {
        val pedidoOpt = pedidoRepository.findById(id)
        if (pedidoOpt.isEmpty) throw RuntimeException("Pedido não encontrado")
        val pedido = pedidoOpt.get()
        val statusAntigo = pedido.statusPedido?.status
        // Normaliza para o padrão do banco
        val statusNormalizado = when (novoStatus.uppercase()) {
            "INICIADO" -> "Em Processamento"
            "PREPARACAO" -> "Em Separação"
            else -> novoStatus
        }
        val statusPedido = statusPedidoRepository.findByStatusIgnoreCase(statusNormalizado)
        if (statusPedido == null) throw RuntimeException("Status de pedido inválido")
        pedido.statusPedido = statusPedido
        pedidoRepository.save(pedido)
        logger.info("Pedido $id: statusPedido de '$statusAntigo' para '${pedido.statusPedido?.status}'")
        return detalharPedido(id)!!
    }

    fun atualizarStatusPagamento(id: Int, novoStatus: String): PedidoDTO {
        val pedidoOpt = pedidoRepository.findById(id)
        if (pedidoOpt.isEmpty) throw RuntimeException("Pedido não encontrado")
        val pedido = pedidoOpt.get()
        // Mapeamento para status de pagamento conforme tabela do banco
        val statusNormalizado = when (novoStatus.uppercase()) {
            "APROVADO" -> "Aprovado"
            "PENDENTE" -> "Pendente"
            "RECUSADO" -> "Recusado"
            "ESTORNADO" -> "Estornado"
            else -> novoStatus.replaceFirstChar { it.uppercase() }
        }
        val statusPagamento = statusPagamentoRepository.findByStatusIgnoreCase(statusNormalizado)
        if (statusPagamento == null) throw RuntimeException("Status de pagamento inválido")
        pedido.statusPagamento = statusPagamento
        pedidoRepository.save(pedido)
        logger.info("Pedido $id atualizado para statusPagamento: ${pedido.statusPagamento?.status}")
        return detalharPedido(id)!!
    }

    fun criarPedido(pedidoDTO: PedidoDTO): PedidoDTO {
        val usuarioOptional = usuarioRepository.findById(pedidoDTO.id ?: 0)
        if (usuarioOptional.isEmpty) {
            throw RuntimeException("Usuário com esse ID não foi encontrado")
        }
        val produtos: List<Produto> = pedidoDTO.itens.map { item ->
            produtoRepository.findById(item.sku?.toIntOrNull() ?: 0)
                .orElseThrow { RuntimeException("Produto com SKU ${item.sku} não foi encontrado") }
        }
        val pedido = Pedido(
            idPedido = null,
            dataPedido = java.time.LocalDate.now(),
            total = pedidoDTO.valorTotal,
            formaPagamento = null,
            usuario = usuarioOptional.get(),
            statusPedido = null,
            statusPagamento = null,
            produtos = produtos
        )
        pedidoRepository.save(pedido)
        return pedidoDTO.copy(id = pedido.idPedido)
    }
    fun listarPedidos(): List<PedidoDTO> {
        val listaPedidos = pedidoRepository.findAll()
        return listaPedidos.map { pedido ->
            val statusPagamentoNormalizado = when (pedido.statusPagamento?.status?.uppercase()) {
                "APROVADO", "PAGO", "CONCLUIDO" -> "Aprovado"
                "PENDENTE", "AGUARDANDO" -> "Pendente"
                "RECUSADO" -> "Recusado"
                "ESTORNADO" -> "Estornado"
                else -> pedido.statusPagamento?.status ?: "Aguardando"
            }
            PedidoDTO(
                id = pedido.idPedido,
                numeroPedido = null,
                dataPedido = pedido.dataPedido.toString(),
                valorTotal = pedido.total,
                formaPagamento = pedido.formaPagamento?.name,
                statusPagamento = statusPagamentoNormalizado,
                statusPedido = pedido.statusPedido?.status,
                cliente = null,
                enderecoEntrega = null,
                itens = pedido.produtos?.map { produto ->
                    ItemPedidoDTO(
                        sku = produto.idProduto?.toString(),
                        nome = produto.nome,
                        quantidade = 1,
                        preco = produto.preco
                    )
                } ?: emptyList(),
                modelos = null,
                tamanho = null,
                cores = null
            )
        }
    }

    fun detalharPedido(id: Int): PedidoDTO? {
        val pedidoOpt = pedidoRepository.findById(id)
        if (pedidoOpt.isEmpty) return null
        val pedido = pedidoOpt.get()
        val statusPagamentoNormalizado = pedido.statusPagamento?.status ?: "Aguardando"
        return PedidoDTO(
            id = pedido.idPedido,
            numeroPedido = null,
            dataPedido = pedido.dataPedido.toString(),
            valorTotal = pedido.total,
            formaPagamento = pedido.formaPagamento?.name,
            statusPagamento = statusPagamentoNormalizado,
            statusPedido = pedido.statusPedido?.status,
            cliente = null,
            enderecoEntrega = null,
            itens = pedido.produtos?.map { produto ->
                ItemPedidoDTO(
                    sku = produto.idProduto?.toString(),
                    nome = produto.nome,
                    quantidade = 1,
                    preco = produto.preco
                )
            } ?: emptyList(),
            modelos = null,
            tamanho = null,
            cores = null
        )
    }

    fun buscarPedidos(
        searchTerm: String?,
        startDate: String?,
        endDate: String?,
        statusPagamento: String?,
        statusPedido: String?
    ): List<PedidoDTO> {
        val pedidos =
            pedidoRepository.buscarPedidosFiltrados(searchTerm, startDate, endDate, statusPagamento, statusPedido)
        val hoje = java.time.LocalDate.now()
        return pedidos.map { pedido ->
            val dataEntrega = pedido.dataPedido
            val statusPag = pedido.statusPagamento?.status ?: "Aguardando"
            PedidoDTO(
                id = pedido.idPedido,
                numeroPedido = null,
                dataPedido = dataEntrega?.format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy")),
                valorTotal = pedido.total,
                formaPagamento = pedido.formaPagamento?.name,
                statusPagamento = statusPag,
                statusPedido = pedido.statusPedido?.status,
                cliente = ClienteResumoDTO(
                    nome = pedido.usuario?.nomeCompleto,
                    telefone = pedido.usuario?.telefone,
                    email = pedido.usuario?.login
                ),
                enderecoEntrega = null,
                itens = pedido.produtos?.map { produto ->
                    ItemPedidoDTO(
                        sku = produto.idProduto?.toString(),
                        nome = produto.nome,
                        quantidade = 1,
                        preco = produto.preco
                    )
                } ?: emptyList(),
                modelos = null,
                tamanho = null,
                cores = null
            )
        }
    }

    fun buscaResumoPedido(idPedido: Int): PedidoResumoDTO? {
        val resultado = pedidoRepository.buscaResumoPedido(idPedido)
        if (resultado.isEmpty()) return null
        val r = resultado.first()
        return PedidoResumoDTO(
            id = (r["id"] as? Number)?.toInt() ?: 0,
            total = r["total"]?.toString() ?: "R$0,00",
            formaPagamento = r["formaPagamento"]?.toString() ?: "Desconhecida",
            formaEnvio = r["formaEnvio"]?.toString() ?: "Vendedor",
            cepEntrega = r["cepEntrega"]?.toString() ?: "00000000"
        )
    }

    fun atualizarPedido(id: Int, pedidoDTO: PedidoDTO): PedidoDTO {
        val pedidoOpt = pedidoRepository.findById(id)
        if (pedidoOpt.isEmpty) {
            throw RuntimeException("Pedido com esse ID não encontrado")
        }
        val pedido = pedidoOpt.get()
        pedido.total = pedidoDTO.valorTotal
        pedido.formaPagamento = null
        pedido.statusPedido = null
        pedido.statusPagamento = null
        pedido.produtos = pedidoDTO.itens.map { item ->
            produtoRepository.findById(item.sku?.toIntOrNull() ?: 0)
                .orElseThrow { RuntimeException("Produto com SKU ${item.sku} não foi encontrado") }
        }
        pedidoRepository.save(pedido)
        return pedidoDTO.copy(id = pedido.idPedido)
    }

    fun excluirPedido(id: Int) {
        val existe = pedidoRepository.existsById(id)
        if (existe) {
            pedidoRepository.deleteById(id)
        } else {
            throw ValidacaoException("Pedido não encontrado")
        }
    }

    @Transactional(readOnly = true)
    fun buscaResumoCompletoPedido(idPedido: Int): PedidoResumoCompletoDTO? {
        val pedido = pedidoRepository.findById(idPedido).orElse(null) ?: return null

        val produtosDTO: List<ProdutoDTO> = pedido.produtos?.map { produto ->
            ProdutoDTO(
                idProduto = produto.idProduto,
                idModelo = produto.modelo?.idModelo,
                nome = produto.nome,
                colecao = produto.modelo?.nomeModelo,
                tamanho = produto.tamanho,
                tipoLaco = produto.tipoLaco,
                material = produto.material,
                cor = produto.cor,
                corDescricao = produto.cor,
                acabamento = produto.acabamento,
                acabamentoDescricao = produto.acabamento,
                foto = produto.modelo?.getFotoBase64(),
                preco = produto.preco
            )
        } ?: emptyList()

        return PedidoResumoCompletoDTO(
            idPedido = pedido.idPedido?.toLong() ?: 0L,
            nomeCliente = pedido.usuario?.nomeCompleto,
            telefone = pedido.usuario?.telefone,
            dataPedido = pedido.dataPedido,
            previsaoEntrega = null,
            total = BigDecimal.valueOf(pedido.total),
            formaPagamento = pedido.formaPagamento,
            statusPagamento = pedido.statusPagamento?.status,
            statusPedido = pedido.statusPedido?.status,
            produtos = produtosDTO
        )
    }

    fun listarMeusPedidos(): List<ListaPedidosDTO> {
        val authentication = SecurityContextHolder.getContext().authentication
        val principal = authentication?.principal
        val username = when (principal) {
            is org.springframework.security.core.userdetails.UserDetails -> principal.username
            is String -> principal
            else -> null
        } ?: throw RuntimeException("Usuário não autenticado")

        val usuario = usuarioRepository.findByEmail(username)
        val rows = pedidoRepository.listarPedidosDoUsuario(usuario.idUsuario!!)
        if (rows.isEmpty()) return emptyList()

        // Agrupamento por pedido -> produto -> caracteristicas
        val pedidosMap = mutableMapOf<Int, MutableMap<Int, MutableList<Map<String, Any>>>>()
        rows.forEach { row ->
            val idPedido = (row["idPedido"] as Number).toInt()
            val idProduto = (row["idProduto"] as Number).toInt()
            val pedidoGroup = pedidosMap.getOrPut(idPedido) { mutableMapOf() }
            val produtoGroup = pedidoGroup.getOrPut(idProduto) { mutableListOf() }
            produtoGroup.add(row)
        }

        return pedidosMap.entries.map { (idPedido, produtosMap) ->
            val primeiraLinhaPedido = produtosMap.values.first().first()
            val dataPedido = primeiraLinhaPedido["dataPedido"].toString().substring(0, 10) // assume LocalDateTime/String
            val statusPedido = primeiraLinhaPedido["statusPedido"]?.toString()
            val statusPagamento = primeiraLinhaPedido["statusPagamento"]?.toString()
            val totalPedido = (primeiraLinhaPedido["totalPedido"] as Number).toDouble()

            val itens = produtosMap.entries.map { (idProduto, linhasProduto) ->
                val base = linhasProduto.first()
                val nomeProduto = base["nomeProduto"]?.toString()
                val precoProduto = (base["precoProduto"] as? Number)?.toDouble()
                val nomeModelo = base["nomeModelo"]?.toString()
                val imagemBase64 = com.lacos_preciosos.preciososLacos.utils.ImageUtils.toBase64(base["fotoModelo"])

                // Características únicas por (nomeCaracteristica, detalheCaracteristica)
                val caracteristicas = linhasProduto.filter { it["nomeCaracteristica"] != null && it["detalheCaracteristica"] != null }
                    .map { r ->
                        CaracteristicaItemDTO(
                            nome = r["nomeCaracteristica"].toString(),
                            detalhe = r["detalheCaracteristica"].toString()
                        )
                    }
                    .distinctBy { it.nome + "|" + it.detalhe }

                ItemPedidoDetalheDTO(
                    idProduto = idProduto,
                    nome = nomeProduto,
                    modelo = nomeModelo,
                    quantidade = 1, // não há quantidade na join table atual
                    preco = precoProduto,
                    imagens = imagemBase64?.let { listOf(it) } ?: emptyList(),
                    caracteristicas = caracteristicas
                )
            }

            ListaPedidosDTO(
                idPedido = idPedido,
                data = dataPedido,
                statusPedido = statusPedido,
                statusPagamento = statusPagamento,
                total = totalPedido,
                itens = itens
            )
        }.sortedBy { it.idPedido }
    }

}
