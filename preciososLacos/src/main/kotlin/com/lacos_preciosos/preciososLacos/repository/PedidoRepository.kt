package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.Pedido
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PedidoRepository : JpaRepository<Pedido, Int> {
    // Retorna o primeiro pedido marcado como carrinho para um usuário
    fun findFirstByUsuarioIdUsuarioAndCarrinhoTrue(idUsuario: Int): Pedido?

    @Query(
        value = """
        SELECT p.* FROM pedido p
        JOIN usuario u ON p.usuario_id_usuario = u.id_usuario
        JOIN status_pagamento sp ON p.status_pagamento_id_status_pagamento = sp.id_status_pagamento
        JOIN status_pedido st ON p.status_pedido_id_status_pedido = st.id_status_pedido
        WHERE (:searchTerm IS NULL OR u.nome_completo LIKE CONCAT('%', :searchTerm, '%'))
          AND (:statusPagamento IS NULL OR sp.status = :statusPagamento)
          AND (:statusPedido IS NULL OR st.status = :statusPedido)
          AND (:startDate IS NULL OR p.data_pedido >= :startDate)
          AND (:endDate IS NULL OR p.data_pedido <= :endDate)
        ORDER BY p.id_pedido
        """,
        nativeQuery = true
    )
    fun buscarPedidosFiltrados(
        searchTerm: String?,
        startDate: String?,
        endDate: String?,
        statusPagamento: String?,
        statusPedido: String?
    ): List<Pedido>

    @Query(
        value = """
        SELECT 
            u.nome_completo AS nomeCliente,
            u.telefone AS telefone,
            DATE_FORMAT(p.data_pedido, '%d %b %Y') AS dataPedido,
            p.total AS total,
            CASE p.forma_pagamento 
                WHEN 1 THEN 'Débito'
                WHEN 2 THEN 'Crédito'
                ELSE 'Outro'
            END AS formaPagamento,
            sp.status AS statusPagamento,
            st.status AS statusPedido
        FROM pedido p
        JOIN usuario u ON p.usuario_id_usuario = u.id_usuario
        JOIN status_pagamento sp ON p.status_pagamento_id_status_pagamento = sp.id_status_pagamento
        JOIN status_pedido st ON p.status_pedido_id_status_pedido = st.id_status_pedido
        ORDER BY p.id_pedido
    """,
        nativeQuery = true
    )
    fun findAllPedidos(): List<Map<String, Any>>

    @Query(
        value = """
        SELECT 
            p.id_pedido AS id,
            CONCAT('R$', FORMAT(p.total, 2, 'pt_BR')) AS total,
            CASE p.forma_pagamento 
                WHEN 1 THEN 'Débito'
                WHEN 2 THEN 'Crédito'
                WHEN 3 THEN 'Pix'
                ELSE 'Outro'
            END AS formaPagamento,
            'Vendedor' AS formaEnvio,
            e.cep AS cepEntrega
        FROM pedido p
        JOIN endereco e ON e.usuario_id_usuario = p.usuario_id_usuario
        WHERE p.id_pedido = :idPedido
    """,
        nativeQuery = true
    )
    fun buscaResumoPedido(idPedido: Int): List<Map<String, Any>>

    @Query(
        """
     SELECT 
        p.id_pedido,
        u.nome_completo AS cliente,
        sp.status AS status,
        DATE_ADD(p.data_pedido, INTERVAL 7 DAY) AS previsao_entrega
        FROM pedido p
        JOIN usuario u ON u.id_usuario = p.usuario_id_usuario
        JOIN status_pedido sp ON sp.id_status_pedido = p.status_pedido_id_status_pedido
        WHERE DATE_ADD(p.data_pedido, INTERVAL 7 DAY) = CURDATE();
    """, nativeQuery = true
    )
    fun listarEntregasDoDia(): List<Map<String, Any>>

    @Query(
        value = """
        SELECT 
    DATE_FORMAT(p.data_pedido, '%a') AS dia_semana,
    SUM(p.total) AS total
FROM pedido p
WHERE p.data_pedido >= CURDATE() - INTERVAL 6 DAY
GROUP BY DATE(p.data_pedido), DATE_FORMAT(p.data_pedido, '%a')
ORDER BY DATE(p.data_pedido)
    """,
        nativeQuery = true
    )
    fun listarVendasUltimos7Dias(): List<Map<String, Any>>

    @Query(
        """
    SELECT COUNT(*) 
    FROM pedido 
    WHERE data_pedido >= NOW() - INTERVAL 1 DAY
""", nativeQuery = true
    )
    fun countPedidosUltimas24h(): Int

    @Query("""
    SELECT COUNT(*) 
    FROM pedido 
    WHERE DATE(data_pedido) = CURDATE() - INTERVAL 1 DAY
""", nativeQuery = true)
    fun countPedidosOntem(): Int

    @Query("""
    SELECT COUNT(*) 
    FROM pedido 
    WHERE status_pedido_id_status_pedido = (
        SELECT id_status_pedido FROM status_pedido WHERE status = 'Em andamento'
    )
    AND DATEDIFF(CURDATE(), data_pedido) <= 7
""", nativeQuery = true)
    fun countEntregasProgramadas(): Int

    @Query("""
    SELECT COUNT(*) 
    FROM pedido 
    WHERE status_pedido_id_status_pedido = (
        SELECT id_status_pedido FROM status_pedido WHERE status = 'Em andamento'
    )
    AND DATEDIFF(CURDATE(), data_pedido) > 7
""", nativeQuery = true)
    fun countEntregasAtrasadas(): Int

    @Query("""
    SELECT IFNULL(AVG(DATEDIFF(CURDATE(), data_pedido) - 7), 0)
    FROM pedido 
    WHERE status_pedido_id_status_pedido = (
        SELECT id_status_pedido FROM status_pedido WHERE status = 'Em andamento'
    )
    AND DATEDIFF(CURDATE(), data_pedido) > 7
""", nativeQuery = true)
    fun atrasoMedioDias(): Double

    @Query("""
    SELECT COUNT(*) 
    FROM pedido 
    WHERE status_pagamento_id_status_pagamento = (
        SELECT id_status_pagamento FROM status_pagamento WHERE status = 'Pendente'
    )
""", nativeQuery = true)
    fun countPedidosPendentes(): Int

    @Query(
        """
    SELECT IFNULL(SUM(total), 0)
    FROM pedido 
    WHERE DATE(data_pedido) = CURDATE()
""", nativeQuery = true
    )
    fun totalVendasDia(): Double

    @Query("""
    SELECT IFNULL(SUM(total), 0)
    FROM pedido 
    WHERE DATE(data_pedido) = CURDATE() - INTERVAL 1 DAY
""", nativeQuery = true)
    fun totalVendasOntem(): Double

    @Query(
        """
    SELECT IFNULL(AVG(total), 0)
    FROM pedido 
    WHERE DATE(data_pedido) = CURDATE()
""", nativeQuery = true
    )
    fun ticketMedio(): Double

    @Query("""
    SELECT IFNULL(AVG(total), 0)
    FROM pedido 
    WHERE DATE(data_pedido) BETWEEN CURDATE() - INTERVAL 13 DAY AND CURDATE() - INTERVAL 7 DAY
""", nativeQuery = true)
    fun ticketMedioSemanaPassada(): Double


    @Query(
        value = """
        SELECT 
            u.nome_completo AS nomeCliente,
            u.telefone AS telefone,
            DATE_FORMAT(p.data_pedido, '%d/%m/%Y') AS dataPedido,
            p.total AS total,
            CASE p.forma_pagamento 
                WHEN 1 THEN 'Débito'
                WHEN 2 THEN 'Crédito'
                WHEN 3 THEN 'Pix'
                ELSE 'Outro'
            END AS formaPagamento,
            sp.status AS statusPagamento,
            st.status AS statusPedido
        FROM pedido p
        JOIN usuario u ON u.id_usuario = p.usuario_id_usuario
        JOIN status_pagamento sp ON sp.id_status_pagamento = p.status_pagamento_id_status_pagamento
        JOIN status_pedido st ON st.id_status_pedido = p.status_pedido_id_status_pedido
        WHERE (:dataInicio IS NULL OR :dataInicio = '' OR DATE(p.data_pedido) >= STR_TO_DATE(:dataInicio, '%Y-%m-%d'))
          AND (:dataFim IS NULL OR :dataFim = '' OR DATE(p.data_pedido) <= STR_TO_DATE(:dataFim, '%Y-%m-%d'))
          AND (:statusPagamento IS NULL OR :statusPagamento = '' OR sp.status = :statusPagamento)
          AND (:statusPedido IS NULL OR :statusPedido = '' OR st.status = :statusPedido)
        ORDER BY p.id_pedido DESC
    """,
        nativeQuery = true
    )
    fun listarPedidosFiltrados(
        dataInicio: String?,
        dataFim: String?,
        statusPagamento: String?,
        statusPedido: String?
    ): List<Map<String, Any>>

    @Query(
        value = """
        SELECT 
            COUNT(*) AS pedidos24h,
            IFNULL(SUM(p.total), 0) AS totalVendas,
            IFNULL(AVG(p.total), 0) AS ticketMedio
        FROM pedido p
        JOIN status_pagamento sp ON sp.id_status_pagamento = p.status_pagamento_id_status_pagamento
        JOIN status_pedido st ON st.id_status_pedido = p.status_pedido_id_status_pedido
        WHERE (:dataInicio IS NULL OR :dataInicio = '' OR DATE(p.data_pedido) >= STR_TO_DATE(:dataInicio, '%Y-%m-%d'))
          AND (:dataFim IS NULL OR :dataFim = '' OR DATE(p.data_pedido) <= STR_TO_DATE(:dataFim, '%Y-%m-%d'))
          AND (:statusPagamento IS NULL OR :statusPagamento = '' OR sp.status = :statusPagamento)
          AND (:statusPedido IS NULL OR :statusPedido = '' OR st.status = :statusPedido)
    """,
        nativeQuery = true
    )
    fun resumoFiltrado(
        dataInicio: String?,
        dataFim: String?,
        statusPagamento: String?,
        statusPedido: String?
    ): Map<String, Any>


    @Query(
        value = """
        SELECT 
            p.id_pedido,
            u.nome_completo AS cliente,
            st.status AS status,
            DATE_ADD(p.data_pedido, INTERVAL 7 DAY) AS previsao_entrega
        FROM pedido p
        JOIN usuario u ON u.id_usuario = p.usuario_id_usuario
        JOIN status_pedido st ON st.id_status_pedido = p.status_pedido_id_status_pedido
        WHERE (:dataInicio IS NULL OR :dataInicio = '' OR DATE(p.data_pedido) >= STR_TO_DATE(:dataInicio, '%Y-%m-%d'))
          AND (:dataFim IS NULL OR :dataFim = '' OR DATE(p.data_pedido) <= STR_TO_DATE(:dataFim, '%Y-%m-%d'))
          AND (:statusPagamento IS NULL OR :statusPagamento = '' OR p.status_pagamento_id_status_pagamento IN 
              (SELECT id_status_pagamento FROM status_pagamento WHERE status = :statusPagamento))
          AND (:statusPedido IS NULL OR :statusPedido = '' OR st.status = :statusPedido)
    """,
        nativeQuery = true
    )
    fun listarEntregasFiltradas(
        dataInicio: String?,
        dataFim: String?,
        statusPagamento: String?,
        statusPedido: String?
    ): List<Map<String, Any>>

    @Query(
    value = """
        SELECT 
            DATE_FORMAT(p.data_pedido, '%a') AS dia_semana,
            SUM(p.total) AS total
        FROM pedido p
        JOIN status_pagamento sp ON sp.id_status_pagamento = p.status_pagamento_id_status_pagamento
        JOIN status_pedido st ON st.id_status_pedido = p.status_pedido_id_status_pedido
        WHERE (:dataInicio IS NULL OR :dataInicio = '' OR DATE(p.data_pedido) >= STR_TO_DATE(:dataInicio, '%Y-%m-%d'))
          AND (:dataFim IS NULL OR :dataFim = '' OR DATE(p.data_pedido) <= STR_TO_DATE(:dataFim, '%Y-%m-%d'))
          AND (:statusPagamento IS NULL OR :statusPagamento = '' OR sp.status = :statusPagamento)
          AND (:statusPedido IS NULL OR :statusPedido = '' OR st.status = :statusPedido)
        GROUP BY DATE_FORMAT(p.data_pedido, '%a')
        ORDER BY MIN(p.data_pedido)
    """,
    nativeQuery = true
    )
    fun listarVendasFiltradas(
        dataInicio: String?,
        dataFim: String?,
        statusPagamento: String?,
        statusPedido: String?
    ): List<Map<String, Any>>

    @Query(
        value = """
        SELECT 
            p.id_pedido AS idPedido,
            p.data_pedido AS dataPedido,
            st.status AS statusPedido,
            sp.status AS statusPagamento,
            p.total AS totalPedido,
            pr.id_produto AS idProduto,
            pr.nome AS nomeProduto,
            pr.preco AS precoProduto,
            pr.tamanho AS tamanhoProduto,
            pr.tipo_laco AS tipoLaco,
            pr.acabamento AS acabamento,
            pr.cor AS corProduto,
            m.id_modelo AS idModelo,
            m.nome_modelo AS nomeModelo,
            m.foto AS fotoModelo,
            cd.id_caracteristica_detalhe AS idCaracteristicaDetalhe,
            cd.descricao AS detalheCaracteristica,
            c.descricao AS nomeCaracteristica
        FROM pedido p
        JOIN pedido_produto pp ON p.id_pedido = pp.id_pedido
        JOIN produto pr ON pr.id_produto = pp.id_produto
        LEFT JOIN modelo m ON pr.modelo_id_modelo = m.id_modelo
        LEFT JOIN modelo_caracteristica_detalhe mcd ON mcd.modelo_id_modelo = m.id_modelo
        LEFT JOIN caracteristica_detalhe cd ON cd.id_caracteristica_detalhe = mcd.caracteristica_id_caracteristica_detalhe
        LEFT JOIN caracteristica c ON cd.caracteristica_id_caracteristica = c.id_caracteristica
        LEFT JOIN status_pedido st ON p.status_pedido_id_status_pedido = st.id_status_pedido
        LEFT JOIN status_pagamento sp ON p.status_pagamento_id_status_pagamento = sp.id_status_pagamento
        WHERE p.usuario_id_usuario = :idUsuario AND p.carrinho = false
        ORDER BY p.id_pedido, pr.id_produto
        """,
        nativeQuery = true
    )
    fun listarPedidosDoUsuario(idUsuario: Int): List<Map<String, Any>>

    @Query(
        value = """
        SELECT 
            p.id_pedido AS idPedido,
            p.carrinho AS carrinho,
            pr.id_produto AS idProduto,
            pr.nome AS nomeProduto,
            pr.preco AS precoProduto,
            m.id_modelo AS idModelo,
            m.nome_modelo AS nomeModelo,
            m.foto AS fotoModelo,
            cd.id_caracteristica_detalhe AS idCaracteristicaDetalhe,
            cd.descricao AS detalheCaracteristica,
            c.descricao AS nomeCaracteristica
        FROM pedido p
        LEFT JOIN pedido_produto pp ON p.id_pedido = pp.id_pedido
        LEFT JOIN produto pr ON pr.id_produto = pp.id_produto
        LEFT JOIN modelo m ON pr.modelo_id_modelo = m.id_modelo
        LEFT JOIN modelo_caracteristica_detalhe mcd ON mcd.modelo_id_modelo = m.id_modelo
        LEFT JOIN caracteristica_detalhe cd ON cd.id_caracteristica_detalhe = mcd.caracteristica_id_caracteristica_detalhe
        LEFT JOIN caracteristica c ON cd.caracteristica_id_caracteristica = c.id_caracteristica
        WHERE p.usuario_id_usuario = :idUsuario AND p.carrinho = true
        ORDER BY pr.id_produto
        """,
        nativeQuery = true
    )
    fun buscarCarrinhoDetalhadoPorUsuario(idUsuario: Int): List<Map<String, Any>>
}
