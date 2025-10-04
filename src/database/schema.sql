-- Passo 1: Apaga o banco de dados existente.
-- O "IF EXISTS" é uma segurança para não dar erro se o banco de dados ainda não tiver sido criado.
DROP DATABASE IF EXISTS todolist;

-- Passo 2: Cria um banco de dados limpo e novo.
CREATE DATABASE todo_list_db;

-- Passo 3: Seleciona o banco de dados recém-criado para usar nos próximos comandos.
USE todo_list_db;

-- Passo 4: Cria a tabela de usuários (deve vir antes da tabela de tarefas).
CREATE TABLE usuarios (
      id INT AUTO_INCREMENT PRIMARY KEY,
      login VARCHAR(50) NOT NULL UNIQUE,
      senha VARCHAR(100) NOT NULL
);

-- Passo 5: Cria a tabela de tarefas, já com a referência correta à tabela de usuários.
CREATE TABLE tarefas (
     id INT AUTO_INCREMENT PRIMARY KEY,
     titulo VARCHAR(255) NOT NULL,
     descricao TEXT NULL,
     status ENUM('Pendente', 'Em andamento', 'Concluída') NOT NULL DEFAULT 'Pendente',
     data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     data_vencimento DATE NULL,
     data_conclusao DATETIME NULL,
     id_usuario INT NOT NULL,
     CONSTRAINT fk_usuario
         FOREIGN KEY (id_usuario)
             REFERENCES usuarios(id)
             ON DELETE CASCADE
);

-- (Opcional) Comando para verificar se as tabelas foram criadas corretamente.
SHOW TABLES;