// DAO - classes de acesso ao banco

package dao;

import model.Tarefa;
import util.ConnectionFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TarefaDAO {

    //C - Create
    public void inserir(Tarefa tarefa){
        String sql = "INSERT INTO tarefas (titulo, descricao, status, data_criacao, data_conclusao) VALUES (?, ?, ?, ?, ?)";

        try(Connection conn = ConnectionFactory.getConnection();) {
            PreparedStatement stmt = conn.prepareStatement(sql); //PreparedStatement serve para enviar comandos SQL para o bando de dados de forma segura e eficiente.
                                                                //Com o PreparedStatement escrevemos o SQL com ? (placeholders) e depois passa os valores
            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setString(3, tarefa.getStatus());
            //stmt.setDate(4, Date.valueOf(tarefa.getData_criacao()));
            //stmt.setDate(5, Date.valueOf(tarefa.getData_conclusao()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //R - Read
    public List<Tarefa> listar(){
        return null;
    }
    //U - Update
    public void atualizar(Tarefa tarefa){

    }

    //D - Delete
    public void remover(Tarefa tarefa){

    }

}

