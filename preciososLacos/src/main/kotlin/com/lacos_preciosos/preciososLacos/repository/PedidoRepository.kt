package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.Pedido
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PedidoRepository : JpaRepository<Pedido, Int> {

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

    @Query(
        """
    SELECT COUNT(*) 
    FROM pedido 
    WHERE status_pedido_id_status_pedido = 1
""", nativeQuery = true
    )
    fun countEntregasProgramadas(): Int

    @Query(
        """
    SELECT COUNT(*) 
    FROM pedido 
    WHERE status_pedido_id_status_pedido = 1
      AND data_pedido < CURDATE() - INTERVAL 1 DAY
""", nativeQuery = true
    )
    fun countEntregasAtrasadas(): Int

    @Query(
        """
    SELECT COUNT(*) 
    FROM pedido 
    WHERE status_pagamento_id_status_pagamento = 1 -- Pendente
""", nativeQuery = true
    )
    fun countPedidosPendentes(): Int

    @Query(
        """
    SELECT IFNULL(SUM(total), 0)
    FROM pedido 
    WHERE DATE(data_pedido) = CURDATE()
""", nativeQuery = true
    )
    fun totalVendasDia(): Double

    @Query(
        """
    SELECT IFNULL(AVG(total), 0)
    FROM pedido 
    WHERE DATE(data_pedido) = CURDATE()
""", nativeQuery = true
    )
    fun ticketMedio(): Double

}
