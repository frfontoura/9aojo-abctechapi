create table assists(
    id_assists bigint auto_increment primary key,
    des_assistance varchar(300) not null,
    nam_assistance varchar(100) not null,
    cod_assistance varchar(36) not null DEFAULT (UUID()),
    dat_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    dat_update DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO assists (nam_assistance, des_assistance, cod_assistance) VALUES ('Troca de aparelho', 'Troca de aparelho decodificador de sinal', '69b6028b-a98d-4578-a996-6eb1da5fb63f');
INSERT INTO assists (nam_assistance, des_assistance, cod_assistance) VALUES ('Troca de cabo interno', 'Troca de cabo interno', '252c5cba-3bc4-4a44-9e48-696cd04a36bd');
INSERT INTO assists (nam_assistance, des_assistance, cod_assistance) VALUES ('Troca de fiação interna', 'Substituição de fiação interna da residência', '7838d3e1-0a9f-4d5f-8eaf-544b6d68b221');
INSERT INTO assists (nam_assistance, des_assistance, cod_assistance) VALUES ('Manutenção em fogão', 'Reparo sem necessidade de compra de peças', '6ac9af25-bcf7-4e84-a7a7-df42a795a2fd');
INSERT INTO assists (nam_assistance, des_assistance, cod_assistance) VALUES ('Manutenção em geladeira', 'Reparo sem necessidade de compra de peças', 'eb228259-4f4b-4f57-9ee0-c9f8c7db2628');
INSERT INTO assists (nam_assistance, des_assistance, cod_assistance) VALUES ('Manutenção em máquina de lavar', 'Reparo sem necessidade de compra de peças', 'd8e942fa-7803-4def-9610-e7a0a8a3297e');
