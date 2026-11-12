CREATE TABLE tb_ocorrencia (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cidadao_id BIGINT NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    descricao TEXT NOT NULL,
    endereco VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    status_ocorrencia VARCHAR(30) NOT NULL
);