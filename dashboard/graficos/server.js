const express = require('express');
const mysql = require('mysql2/promise');
const path = require('path');

const app = express();
const port = 3005;

const dbConfig = {
  host: 'localhost',
  user: 'root',
  password: '131518',
  database: 'preciosos_lacos',
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
};

let pool;
async function initDB() {
  pool = mysql.createPool(dbConfig);
}
initDB().catch(err => {
  console.error("Erro ao conectar no banco:", err);
  process.exit(1);
});

async function query(sql, params = []) {
  const [rows] = await pool.execute(sql, params);
  return rows;
}

const views = {
  'top5-lacos': 'vw_top5_lacos_mais_vendidos',
  'vendas-mensais': 'vw_total_vendas_6_meses',
  'clientes-novos': 'vw_novos_clientes_6_meses',
  'clientes-recompra': 'vw_clientes_recompra_6_meses',
  'formas-pagamento': 'vw_formas_pagamento_6_meses',
  'pedidos-status': 'vw_total_pedidos_6_meses'
};

for (const [endpoint, view] of Object.entries(views)) {
  app.get(`/api/${endpoint}`, async (req, res) => {
    try {
      const rows = await query(`SELECT * FROM ${view}`);
      res.json(rows);
    } catch (err) {
      console.error(`Erro ao consultar ${view}:`, err);
      res.status(500).json({ error: 'Erro no servidor' });
    }
  });
}

app.use(express.static(path.join(__dirname, 'public')));

app.listen(port, () => {
  console.log(`Servidor rodando em http://localhost:${port}/dashboard-V2.html`);
});
