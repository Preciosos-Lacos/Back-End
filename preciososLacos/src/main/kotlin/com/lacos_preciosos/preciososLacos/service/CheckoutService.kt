package com.lacos_preciosos.preciososLacos.service

import com.lacos_preciosos.preciososLacos.dto.pedido.CheckoutCaracteristicaDTO
import com.lacos_preciosos.preciososLacos.dto.pedido.CheckoutEnderecoDTO
import com.lacos_preciosos.preciososLacos.dto.pedido.CheckoutItemDTO
import com.lacos_preciosos.preciososLacos.dto.pedido.CheckoutResumoDTO
import com.lacos_preciosos.preciososLacos.dto.pedido.FinalizarPedidoRequest
import com.lacos_preciosos.preciososLacos.model.Pedido
import com.lacos_preciosos.preciososLacos.model.Usuario
import com.lacos_preciosos.preciososLacos.repository.EnderecoRepository
import com.lacos_preciosos.preciososLacos.repository.PedidoRepository
import com.lacos_preciosos.preciososLacos.repository.StatusPagamentoRepository
import com.lacos_preciosos.preciososLacos.repository.StatusPedidoRepository
import com.lacos_preciosos.preciososLacos.repository.UsuarioRepository
import com.lacos_preciosos.preciososLacos.tipos.TipoPagamento
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class CheckoutService(
    private val pedidoRepository: PedidoRepository,
    private val usuarioRepository: UsuarioRepository,
    private val enderecoRepository: EnderecoRepository,
    private val statusPagamentoRepository: StatusPagamentoRepository,
    private val statusPedidoRepository: StatusPedidoRepository
) {

    private val formasPagamentoFixas = listOf("Pix", "Crédito", "Débito")

    @Transactional
    fun obterCheckout(idUsuario: Int): CheckoutResumoDTO {
        val usuario: Usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow { IllegalArgumentException("Usuário não encontrado") }

        // Garante que exista um pedido carrinho=true para o usuário
        var pedidoCarrinho = pedidoRepository.findFirstByUsuarioIdUsuarioAndCarrinhoTrue(idUsuario)
        if (pedidoCarrinho == null) {
            pedidoCarrinho = Pedido(
                idPedido = null,
                dataPedido = LocalDate.now(),
                total = 0.0,
                formaPagamento = null,
                usuario = usuario,
                statusPedido = null,
                statusPagamento = null,
                produtos = emptyList(),
                carrinho = true
            )
            pedidoCarrinho = pedidoRepository.save(pedidoCarrinho)
        }

        val rows = pedidoRepository.buscarCarrinhoDetalhadoPorUsuario(idUsuario)

        // Agrupar por produto
        val itensMap = mutableMapOf<Int, MutableList<Map<String, Any>>>()
        rows.forEach { row ->
            val idProduto = (row["idProduto"] as? Number)?.toInt() ?: return@forEach
            val list = itensMap.getOrPut(idProduto) { mutableListOf() }
            list.add(row)
        }

        val itens = itensMap.entries.map { (idProduto, linhasProduto) ->
            val base = linhasProduto.first()
            val nomeProduto = base["nomeProduto"]?.toString()
            val precoUnitario = (base["precoProduto"] as? Number)?.toDouble() ?: 0.0
            val nomeModelo = base["nomeModelo"]?.toString()
            val fotoBase64 = com.lacos_preciosos.preciososLacos.utils.ImageUtils.toBase64(base["fotoModelo"])

            val quantidade = linhasProduto.size
            val precoTotal = precoUnitario * quantidade

            val caracteristicas = linhasProduto
                .filter { it["nomeCaracteristica"] != null && it["detalheCaracteristica"] != null }
                .map { r ->
                    CheckoutCaracteristicaDTO(
                        nome = r["nomeCaracteristica"].toString(),
                        detalhe = r["detalheCaracteristica"].toString()
                    )
                }
                .distinctBy { it.nome + "|" + it.detalhe }

            CheckoutItemDTO(
                idProduto = idProduto,
                nome = nomeProduto,
                modelo = nomeModelo,
                quantidade = quantidade,
                precoUnitario = precoUnitario,
                precoTotal = precoTotal,
                imagemPrincipal = fotoBase64,
                caracteristicas = caracteristicas
            )
        }.sortedBy { it.idProduto }

        val subtotal = itens.sumOf { it.precoTotal }
        val fretePadrao = 15.0
        val total = subtotal + fretePadrao

        // Endereço principal (primeiro endereço do usuário, se existir)
        val endereco = enderecoRepository.findByUsuario_IdUsuario(idUsuario).firstOrNull()
        val enderecoDTO = endereco?.let {
            CheckoutEnderecoDTO(
                logradouro = it.logradouro,
                numero = it.numero,
                complemento = it.complemento,
                bairro = it.bairro,
                localidade = it.localidade,
                uf = it.uf,
                cep = it.cep
            )
        }

        return CheckoutResumoDTO(
            idPedido = pedidoCarrinho.idPedido!!,
            carrinho = true,
            produtos = itens,
            endereco = enderecoDTO,
            subtotal = subtotal,
            frete = fretePadrao,
            total = total,
            formasPagamento = formasPagamentoFixas
        )
    }

    @Transactional
    fun finalizarPedido(request: FinalizarPedidoRequest): Map<String, Any?> {
        val idUsuario = request.idUsuario ?: throw IllegalArgumentException("idUsuario é obrigatório")
        val formaPagamentoInt = request.formaPagamento ?: throw IllegalArgumentException("formaPagamento é obrigatório")
        val frete = request.frete ?: 15.0

        val usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow { IllegalArgumentException("Usuário não encontrado") }

        val pedidoCarrinho = pedidoRepository.findFirstByUsuarioIdUsuarioAndCarrinhoTrue(idUsuario)
            ?: throw IllegalArgumentException("Nenhum carrinho encontrado para o usuário")

        // Recalcula subtotal com base nos produtos atuais do carrinho
        val produtos = pedidoCarrinho.produtos ?: emptyList()
        if (produtos.isEmpty()) {
            throw IllegalArgumentException("Carrinho vazio")
        }
        val subtotal = produtos.sumOf { it.preco }
        val total = subtotal + frete

        // Determinar status de pagamento pendente e status de pedido em andamento
        val statusPagamentoPendente = statusPagamentoRepository.findByStatusIgnoreCase("Pendente")
            ?: throw IllegalStateException("Status de pagamento 'Pendente' não encontrado")
        val statusPedidoEmAndamento = statusPedidoRepository.findByStatusIgnoreCase("Em andamento")
            ?: throw IllegalStateException("Status de pedido 'Em andamento' não encontrado")

        // Atualizar pedido atual
        pedidoCarrinho.carrinho = false
        pedidoCarrinho.total = total
        pedidoCarrinho.statusPagamento = statusPagamentoPendente
        pedidoCarrinho.statusPedido = statusPedidoEmAndamento
        pedidoCarrinho.formaPagamento = TipoPagamento.getTipoPagamento(formaPagamentoInt.toString())

        pedidoRepository.save(pedidoCarrinho)

        // Cria novo carrinho vazio para o usuário
        var novoCarrinho = Pedido(
            idPedido = null,
            dataPedido = LocalDate.now(),
            total = 0.0,
            formaPagamento = null,
            usuario = usuario,
            statusPedido = null,
            statusPagamento = null,
            produtos = emptyList(),
            carrinho = true
        )
        pedidoRepository.save(novoCarrinho)

        return mapOf(
            "id" to (pedidoCarrinho.idPedido ?: 0),
            "total" to total,
            "statusPagamento" to pedidoCarrinho.statusPagamento?.status,
            "statusPedido" to pedidoCarrinho.statusPedido?.status
        )
    }
}
