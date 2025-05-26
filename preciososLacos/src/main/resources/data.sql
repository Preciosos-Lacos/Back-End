INSERT INTO tipo_usuario (idTipoUsuario, tipo_usuario) VALUES
    (DEFAULT, 'Beneficiario'),
    (DEFAULT, 'Cliente'),;

INSERT INTO status_pagamento (idStatusPagamento, status) VALUES
    (DEFAULT, 'Pendente'),
    (DEFAULT, 'Aguardando'),
    (DEFAULT, 'Concluído'),;

INSERT INTO status_pedido (idStatusPedido, status) VALUES
    (DEFAULT, 'Aguardando'),
    (DEFAULT, 'Iniciado'),
    (DEFAULT, 'Concluído'),;

INSERT INTO caracteristica (idCaracteristica, descricao) VALUES
    (DEFAULT, 'COR'),
    (DEFAULT, 'TAMANHO'),
    (DEFAULT, 'ACABAMENTO'),
    (DEFAULT, 'COLEÇÃO'),
    (DEFAULT, 'TIPO DE LAÇO'),;
