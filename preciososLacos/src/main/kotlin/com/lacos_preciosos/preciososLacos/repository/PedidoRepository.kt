package com.lacos_preciosos.preciososLacos.repository

import com.lacos_preciosos.preciososLacos.model.Pedido
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Date

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
    """
    , nativeQuery = true)
    fun listarEntregasDoDia(): List<Map<String, Any>>

}