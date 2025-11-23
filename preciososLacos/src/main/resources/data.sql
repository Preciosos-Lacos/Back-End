INSERT INTO tipo_usuario (idTipoUsuario, tipo_usuario) VALUES
    (DEFAULT, 'Beneficiario'),
    (DEFAULT, 'Cliente');

INSERT INTO status_pagamento (id_status_pagamento, status) VALUES
    (DEFAULT, 'Pendente'),
    (DEFAULT, 'Pago'),
    (DEFAULT, 'Cancelado'),
    (DEFAULT, 'Estornado');

INSERT INTO status_pedido (id_status_pedido, status) VALUES
    (DEFAULT, 'Em andamento'),
    (DEFAULT, 'Entregue'),
    (DEFAULT, 'Cancelado'),
    (DEFAULT, 'Concluido');

INSERT INTO caracteristica (id_caracteristica, descricao) VALUES
    (DEFAULT, 'COR'),
    (DEFAULT, 'TAMANHO'),
    (DEFAULT, 'ACABAMENTO'),
    (DEFAULT, 'COLEÇÃO'),
    (DEFAULT, 'TIPO DE LAÇO');
