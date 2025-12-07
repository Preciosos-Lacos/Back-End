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

// Import para mapear usuario para DTO de resposta
import com.lacos_preciosos.preciososLacos.dto.usuario.UsuarioResponseDTO

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
        println("[DEBUG] Usuario encontrado: $usuario")

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
        println("[DEBUG] Endereco encontrado: $endereco")
        val enderecoDTO = endereco?.let {
            println("[DEBUG] CEP do endereco: ${'$'}{it.cep}")
            println("[DEBUG] Endereco.idEndereco = ${'$'}{it.idEndereco} | usuario.idUsuario = ${'$'}{it.usuario?.idUsuario}")
            CheckoutEnderecoDTO(
                idEndereco = it.idEndereco,
                logradouro = it.logradouro,
                numero = it.numero,
                complemento = it.complemento,
                bairro = it.bairro,
                localidade = it.localidade,
                uf = it.uf,
                cep = it.cep,
                usuario = it.usuario?.let { u ->
                    UsuarioResponseDTO(
                        idUsuario = u.idUsuario,
                        nomeCompleto = u.nomeCompleto,
                        login = u.login,
                        senha = null,
                        cpf = u.cpf,
                        telefone = u.telefone,
                        role = u.role,
                        data_cadastro = u.data_cadastro.toString(),
                        fotoPerfil = u.getFotoPerfilBase64()
                    )
                }
            )
        }

        val checkoutResumo = CheckoutResumoDTO(
            idPedido = pedidoCarrinho.idPedido!!,
            carrinho = true,
            produtos = itens,
            endereco = enderecoDTO,
            subtotal = subtotal,
            frete = fretePadrao,
            total = total,
            formasPagamento = formasPagamentoFixas
        )
        println("[DEBUG] CheckoutResumoDTO gerado: $checkoutResumo")
        return checkoutResumo
    }

    @Transactional
    fun finalizarPedido(request: FinalizarPedidoRequest): Map<String, Any?> {
            // Definir forma de envio padrão
            val formaEnvioPadrao = "Correio"
        val idUsuario = request.idUsuario ?: throw IllegalArgumentException("idUsuario é obrigatório")
        val idEndereco = request.idEndereco
        val cepEnviado = request.cep
        val endereco = if (idEndereco != null) {
            val end = enderecoRepository.findById(idEndereco).orElse(null)
            if (end != null && cepEnviado != null && cepEnviado.isNotBlank()) {
                end.cep = cepEnviado
                enderecoRepository.save(end)
            }
            end
        } else {
            val end = enderecoRepository.findByUsuario_IdUsuario(idUsuario).firstOrNull()
            if (end != null && cepEnviado != null && cepEnviado.isNotBlank()) {
                end.cep = cepEnviado
                enderecoRepository.save(end)
            }
            end
        }
        val formaPagamentoInt = request.formaPagamento ?: throw IllegalArgumentException("formaPagamento é obrigatório")
        val frete = request.frete ?: 15.0

        val usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow { IllegalArgumentException("Usuário não encontrado") }

        val pedidoCarrinho = pedidoRepository.findFirstByUsuarioIdUsuarioAndCarrinhoTrue(idUsuario)
            ?: throw IllegalArgumentException("Nenhum carrinho encontrado para o usuário")

        // Recalcula subtotal com base nos dados detalhados do carrinho
        val rows = pedidoRepository.buscarCarrinhoDetalhadoPorUsuario(idUsuario)
        println("[DEBUG] buscarCarrinhoDetalhadoPorUsuario retornou ${rows.size} linhas para usuario $idUsuario")
        rows.forEachIndexed { idx, row ->
            val preco = row["precoProduto"]
            println("[DEBUG] Linha $idx: precoProduto = $preco | dados = $row")
        }
        if (rows.isEmpty()) {
            throw IllegalArgumentException("Carrinho vazio")
        }
        val subtotal = rows.sumOf { (it["precoProduto"] as? Number)?.toDouble() ?: 0.0 }
        println("[DEBUG] Subtotal calculado: $subtotal | Frete: $frete | Total: ${subtotal + frete}")
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
            pedidoCarrinho.formaEnvio = formaEnvioPadrao
        pedidoCarrinho.endereco = endereco

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
            "statusPedido" to pedidoCarrinho.statusPedido?.status,
            "formaPagamento" to pedidoCarrinho.formaPagamento?.tipo,
            "formaEnvio" to "Enviado pela fornecedora Preciosos Laços",
            "cepEntrega" to (endereco?.cep ?: "Não especificado"),
            "dataPedido" to pedidoCarrinho.dataPedido?.toString(),
            "prazoEntrega" to "5 a 7 dias úteis"
        )
    }
}
