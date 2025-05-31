const express = require('express');
const mysql = require('mysql2');
const cors = require('cors');
const path = require('path');

const app = express();
app.use(cors());

const db = mysql.createConnection({
  host: 'localhost',
  port: 3306,
  user: 'root',
  password: '131518',
  database: 'preciosos_lacos'
});

function calcularDiferencaPercentual(atual, anterior) {
  if (anterior === 0) return atual === 0 ? 0 : 100;
  return ((atual - anterior) / anterior) * 100;
}

app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'pre-dashboard.html'));
});

app.get('/api/kpis', async (req, res) => {
  const kpis = {};

  const query = (sql) => new Promise((resolve, reject) => {
    db.query(sql, (err, result) => {
      if (err) reject(err);
      else resolve(result);
    });
  });

  try {
    // Total vendas atual vs anterior (assumindo que view kpi_comparativo_total_vendas tem total_atual e total_anterior)
    const vendas = await query(`SELECT * FROM kpi_comparativo_total_vendas`);
    const vendasAtual = vendas[0]?.total_atual || 0;
    const vendasAnterior = vendas[0]?.total_anterior || 0;
    kpis.totalVendasAtual = vendasAtual;
    kpis.totalVendasAnterior = vendasAnterior;

    // Clientes novos atual vs anterior
    const novos = await query(`SELECT * FROM kpi_comparativo_clientes_novos`);
    const novosAtual = novos[0]?.novos_atual || 0;
    const novosAnterior = novos[0]?.novos_anterior || 0;
    kpis.clientesNovosAtual = novosAtual;
    kpis.clientesNovosAnterior = novosAnterior;

    // Clientes com recompra atual vs anterior
    const recompra = await query(`SELECT * FROM kpi_comparativo_clientes_recompra`);
    const recompraAtual = recompra[0]?.recompra_atual || 0;
    const recompraAnterior = recompra[0]?.recompra_anterior || 0;
    kpis.clientesRecompraAtual = recompraAtual;
    kpis.clientesRecompraAnterior = recompraAnterior;

    // Top 3 formas de pagamento com crescimento percentual
    const formas = await query(`SELECT * FROM kpi_top3_forma_pagamento_comparativo`);
    kpis.formasPagamentoTop3 = formas.map(row => {
      const crescimento = calcularDiferencaPercentual(row.qtd_atual, row.qtd_anterior);
      return {
        nome: row.forma_pagamento,
        qtd_atual: row.qtd_atual,
        qtd_anterior: row.qtd_anterior,
        crescimento_percentual: crescimento.toFixed(2)
      };
    });

    // Forma de pagamento mais usada atual e anterior (exemplo simples, você pode ajustar)
    // Pega a forma que teve maior qtd_atual na lista
    if (formas.length > 0) {
      kpis.pagamentoPopularAtual = formas[0].forma_pagamento;
      kpis.pagamentoPopularAnterior = formas[0].qtd_anterior > 0 ? formas[0].forma_pagamento : 'N/A';
    } else {
      kpis.pagamentoPopularAtual = 'N/A';
      kpis.pagamentoPopularAnterior = 'N/A';
    }

    // Top 5 Laços mais vendidos (ajustar para usar view comparativa se quiser, por simplicidade trouxe direto)
    const top5 = await query(`SELECT modelo, quantidade_atual FROM kpi_top5_lacos_comparativo;`);
    kpis.top5 = top5.map(row => `${row.modelo} (${row.quantidade_atual} vendidos)`);

    res.json(kpis);

  } catch (error) {
    console.error('❌ Erro ao carregar KPIs:', error);
    res.status(500).json({ erro: 'Erro ao buscar KPIs', detalhes: error.message });
  }
});

app.listen(3000, () => console.log('✅ API rodando em http://localhost:3000'));
