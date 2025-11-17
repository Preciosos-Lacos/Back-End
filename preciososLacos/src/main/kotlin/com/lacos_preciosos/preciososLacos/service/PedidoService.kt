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
import com.lacos_preciosos.preciososLacos.validacao.ValidacaoException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class PedidoService(
    val pedidoRepository: PedidoRepository,
    val usuarioRepository: UsuarioRepository,
    val produtoRepository: ProdutoRepository,
    val statusPagamentoRepository: StatusPagamentoRepository,
    val statusPedidoRepository: StatusPedidoRepository
) {

    fun atualizarStatusPedido(id: Int, novoStatus: String): PedidoDTO {
        val pedidoOpt = pedidoRepository.findById(id)
        if (pedidoOpt.isEmpty) throw RuntimeException("Pedido não encontrado")
        val pedido = pedidoOpt.get()
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
        val statusPagamentoNormalizado = when (pedido.statusPagamento?.status?.uppercase()) {
            "APROVADO", "PAGO", "CONCLUIDO" -> "Aprovado"
            "PENDENTE", "AGUARDANDO" -> "Pendente"
            "RECUSADO" -> "Recusado"
            "ESTORNADO" -> "Estornado"
            else -> pedido.statusPagamento?.status ?: "Aguardando"
        }
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
            val statusPag = when (pedido.statusPagamento?.status?.uppercase()) {
                "APROVADO", "PAGO", "CONCLUIDO" -> "Aprovado"
                "RECUSADO" -> "Recusado"
                "ESTORNADO" -> "Estornado"
                "PENDENTE", "AGUARDANDO" -> {
                    if (dataEntrega != null && dataEntrega.isBefore(hoje)) {
                        "Atrasado"
                    } else {
                        "Pendente"
                    }
                }

                else -> pedido.statusPagamento?.status ?: "Aguardando"
            }
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
                    email = pedido.usuario?.email
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
                nome = produto.nome,
                colecao = produto.modelo?.nomeModelo,
                tamanho = produto.tamanho,
                tipoLaco = produto.tipoLaco,
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

}