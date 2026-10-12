-- Estrutura inicial do StockTrace V2.
-- Aplicação Spring Boot com Spring Data JPA.
-- Destinado à instalação em um banco novo.
-- Não altera nem migra tabelas já existentes.

CREATE DATABASE IF NOT EXISTS stocktrace
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE stocktrace;

CREATE TABLE IF NOT EXISTS loja
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    nome        VARCHAR(150) NOT NULL,
    cidade      VARCHAR(150) NOT NULL,
    bairro      VARCHAR(150) NOT NULL,
    rua         VARCHAR(255) NOT NULL,
    numero      VARCHAR(30)  NOT NULL,
    complemento VARCHAR(255),
    cep         VARCHAR(20)  NOT NULL,

    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS produto
(
    id             BIGINT         NOT NULL AUTO_INCREMENT,
    codigo         VARCHAR(50)    NOT NULL,
    nome           VARCHAR(100)   NOT NULL,
    descricao      VARCHAR(255)   NOT NULL,
    preco          DECIMAL(10, 2) NOT NULL,
    unidade_medida VARCHAR(20)    NOT NULL,
    ativo          BOOLEAN        NOT NULL DEFAULT TRUE,
    categoria      VARCHAR(30)    NOT NULL,

    PRIMARY KEY (id),

    CONSTRAINT uq_produto_codigo
        UNIQUE (codigo),

    CONSTRAINT ck_produto_preco
        CHECK (preco > 0),

    CONSTRAINT ck_produto_ativo
        CHECK (ativo IN (0, 1)),

    CONSTRAINT ck_produto_categoria
        CHECK (categoria IN (
                             'MERCEARIA',
                             'HORTIFRUTI',
                             'ACOUGUE_E_FRIOS',
                             'PADARIA',
                             'BEBIDAS',
                             'HIGIENE_E_LIMPEZA',
                             'OUTROS'
            ))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS estoque
(
    id               BIGINT NOT NULL AUTO_INCREMENT,
    produto_id       BIGINT NOT NULL,
    loja_id          BIGINT NOT NULL,
    quantidade_atual INT    NOT NULL DEFAULT 0,

    PRIMARY KEY (id),

    CONSTRAINT uq_estoque_produto_loja
        UNIQUE (produto_id, loja_id),

    KEY idx_estoque_loja (loja_id),

    CONSTRAINT fk_estoque_produto
        FOREIGN KEY (produto_id)
            REFERENCES produto (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    CONSTRAINT fk_estoque_loja
        FOREIGN KEY (loja_id)
            REFERENCES loja (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    CONSTRAINT ck_estoque_quantidade
        CHECK (quantidade_atual >= 0)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS movimentacao
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    estoque_id  BIGINT       NOT NULL,
    tipo        VARCHAR(7)   NOT NULL,
    quantidade  INT          NOT NULL,
    data_hora   DATETIME(6)   NOT NULL,
    motivo      VARCHAR(255) NOT NULL,
    observacao  VARCHAR(255),
    responsavel VARCHAR(150) NOT NULL,

    PRIMARY KEY (id),

    KEY idx_movimentacao_estoque_data (estoque_id, data_hora),

    CONSTRAINT fk_movimentacao_estoque
        FOREIGN KEY (estoque_id)
            REFERENCES estoque (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    CONSTRAINT ck_movimentacao_tipo
        CHECK (tipo IN ('ENTRADA', 'SAIDA')),

    CONSTRAINT ck_movimentacao_quantidade
        CHECK (quantidade > 0)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;