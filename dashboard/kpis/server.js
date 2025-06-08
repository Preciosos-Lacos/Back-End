const express = require('express');
const mysql = require('mysql2');
const cors = require('cors');
const path = require('path');

const app = express();
const port = 3000;

app.use(cors());

app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'dashboardKpi.html'));
});

const db = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '131518',
  database: 'preciosos_lacos'
});

db.connect((err) => {
  if (err) throw err;
  console.log('Conectado ao MySQL');
});

app.get('/api/kpis', (req, res) => {
  const query = `
    SELECT
      -- Total vendas mês atual
      IFNULL(SUM(CASE WHEN MONTH(data_pedido) = MONTH(CURRENT_DATE()) AND YEAR(data_pedido) = YEAR(CURRENT_DATE()) THEN total END), 0) AS total_vendas_atual,
      -- Total vendas mês anterior
      IFNULL(SUM(CASE WHEN MONTH(data_pedido) = MONTH(CURRENT_DATE() - INTERVAL 1 MONTH) AND YEAR(data_pedido) = YEAR(CURRENT_DATE()) THEN total END), 0) AS total_vendas_anterior,

      -- Novos clientes mês atual
      (SELECT COUNT(DISTINCT id) FROM usuario WHERE MONTH(data_cadastro) = MONTH(CURRENT_DATE()) AND YEAR(data_cadastro) = YEAR(CURRENT_DATE())) AS novos_clientes_atual,
      -- Novos clientes mês anterior
      (SELECT COUNT(DISTINCT id) FROM usuario WHERE MONTH(data_cadastro) = MONTH(CURRENT_DATE() - INTERVAL 1 MONTH) AND YEAR(data_cadastro) = YEAR(CURRENT_DATE())) AS novos_clientes_anterior,

      -- Total pedidos mês atual
      COUNT(CASE WHEN MONTH(data_pedido) = MONTH(CURRENT_DATE()) AND YEAR(data_pedido) = YEAR(CURRENT_DATE()) THEN 1 END) AS total_pedidos_atual,
      -- Total pedidos mês anterior
      COUNT(CASE WHEN MONTH(data_pedido) = MONTH(CURRENT_DATE() - INTERVAL 1 MONTH) AND YEAR(data_pedido) = YEAR(CURRENT_DATE()) THEN 1 END) AS total_pedidos_anterior,

      -- Clientes com recompra mês atual (quantidade)
      (SELECT COUNT(DISTINCT p.usuario_id)
       FROM pedido p
       WHERE MONTH(p.data_pedido) = MONTH(CURRENT_DATE()) AND YEAR(p.data_pedido) = YEAR(CURRENT_DATE())
         AND p.usuario_id IN (
           SELECT usuario_id
           FROM pedido
           WHERE data_pedido < DATE_FORMAT(CURRENT_DATE(), '%Y-%m-01')
           GROUP BY usuario_id
         )
      ) AS clientes_recompra_atual,

      -- Clientes com recompra mês anterior (quantidade)
      (SELECT COUNT(DISTINCT p.usuario_id)
       FROM pedido p
       WHERE MONTH(p.data_pedido) = MONTH(CURRENT_DATE() - INTERVAL 1 MONTH) AND YEAR(p.data_pedido) = YEAR(CURRENT_DATE())
         AND p.usuario_id IN (
           SELECT usuario_id
           FROM pedido
           WHERE data_pedido < DATE_FORMAT(CURRENT_DATE() - INTERVAL 1 MONTH, '%Y-%m-01')
           GROUP BY usuario_id
         )
      ) AS clientes_recompra_anterior

    FROM pedido
    WHERE YEAR(data_pedido) = YEAR(CURRENT_DATE());
  `;

  db.query(query, (err, results) => {
    if (err) {
      console.error('Erro na consulta:', err);
      return res.status(500).json({ error: err.message });
    }

    const row = results[0];

    function calcVariacao(atual, anterior) {
      if (anterior === 0) return atual === 0 ? '0%' : '↑ 100%';
      const variacao = ((atual - anterior) / anterior) * 100;
      const sinal = variacao >= 0 ? '↑' : '↓';
      return `${sinal} ${Math.abs(variacao).toFixed(2)}%`;
    }

    const percRecompraAtual = row.total_pedidos_atual === 0 ? 0 : (row.clientes_recompra_atual / row.total_pedidos_atual) * 100;
    const percRecompraAnterior = row.total_pedidos_anterior === 0 ? 0 : (row.clientes_recompra_anterior / row.total_pedidos_anterior) * 100;

    const response = {
      total_vendas: `R$ ${row.total_vendas_atual.toFixed(2)}`,
      variacao_total_vendas: calcVariacao(row.total_vendas_atual, row.total_vendas_anterior),

      novos_clientes: row.novos_clientes_atual,
      variacao_novos_clientes: calcVariacao(row.novos_clientes_atual, row.novos_clientes_anterior),

      total_pedidos: row.total_pedidos_atual,
      variacao_total_pedidos: calcVariacao(row.total_pedidos_atual, row.total_pedidos_anterior),

      clientes_recompra_percentual: `${percRecompraAtual.toFixed(2)}%`,
      variacao_clientes_recompra_percentual: calcVariacao(percRecompraAtual, percRecompraAnterior),
    };

    res.json(response);
  });
});


app.listen(port, () => {
  console.log(`Servidor rodando em http://localhost:${port}`);
});
