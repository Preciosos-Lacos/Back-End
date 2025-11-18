package com.lacos_preciosos.preciososLacos.service

annotation class PedidoService

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