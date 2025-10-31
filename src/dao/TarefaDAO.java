// DAO - classes de acesso ao banco

package dao;

import model.StatusTarefa;
import model.Tarefa;
import util.ConnectionFactory;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {


    public void inserir(Tarefa tarefa) {
        // data_criacao é definida pelo banco (DEFAULT CURRENT_TIMESTAMP)
        // data_conclusao é nula ao criar
        String sql = "INSERT INTO tarefas (titulo, descricao, status, data_vencimento, id_usuario) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setString(3, tarefa.getStatus().getValorSql()); // 'Pendente', 'Em andamento', 'Concluída'

            // --- Tratamento de Data (LocalDate) ---
            if (tarefa.getDataVencimento() != null) {
                // Converte LocalDate (Java) para Date (SQL)
                stmt.setDate(4, Date.valueOf(tarefa.getDataVencimento()));
            } else {
                // Permite que a data de vencimento seja nula
                stmt.setNull(4, Types.DATE);
            }

            // --- A PARTE MAIS IMPORTANTE ---
            stmt.setInt(5, tarefa.getIdUsuario()); // Vincula a tarefa ao usuário

            stmt.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir tarefa: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Tarefa> listarPorUsuario(int idUsuario) {
        List<Tarefa> tarefas = new ArrayList<>();
        String sql = "SELECT * FROM tarefas WHERE id_usuario = ? ORDER BY data_vencimento ASC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Para cada linha do resultado, cria um objeto Tarefa
                    Tarefa tarefa = new Tarefa();
                    tarefa.setId(rs.getInt("id"));
                    tarefa.setTitulo(rs.getString("titulo"));
                    tarefa.setDescricao(rs.getString("descricao"));
                    tarefa.setStatus(StatusTarefa.fromString(rs.getString("status")));
                    tarefa.setIdUsuario(rs.getInt("id_usuario"));

                    // Converte Timestamp (SQL) para LocalDateTime (Java)
                    tarefa.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());

                    // --- Tratamento de Data (LocalDate) com nulo ---
                    Date sqlDateVenc = rs.getDate("data_vencimento");
                    if (sqlDateVenc != null) {
                        tarefa.setDataVencimento(sqlDateVenc.toLocalDate());
                    }

                    // --- Tratamento de Data (LocalDateTime) com nulo ---
                    Timestamp sqlTimestampConc = rs.getTimestamp("data_conclusao");
                    if (sqlTimestampConc != null) {
                        tarefa.setDataConclusao(sqlTimestampConc.toLocalDateTime());
                    }

                    tarefas.add(tarefa);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar tarefas: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return tarefas;
    }

    public void atualizar(Tarefa tarefa) {
        String sql = "UPDATE tarefas SET titulo = ?, descricao = ?, status = ?, data_vencimento = ?, data_conclusao = ? " +
                "WHERE id = ? AND id_usuario = ?"; // Segurança: só atualiza se for do usuário

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setString(3, tarefa.getStatus().getValorSql());

            // --- Tratamento de Data (LocalDate) ---
            if (tarefa.getDataVencimento() != null) {
                stmt.setDate(4, Date.valueOf(tarefa.getDataVencimento()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            // --- Tratamento de Data (LocalDateTime) ---
            if (tarefa.getDataConclusao() != null) {
                // Converte LocalDateTime (Java) para Timestamp (SQL)
                stmt.setTimestamp(5, Timestamp.valueOf(tarefa.getDataConclusao()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }

            // IDs para o WHERE
            stmt.setInt(6, tarefa.getId());
            stmt.setInt(7, tarefa.getIdUsuario()); // Garante que o usuário só edite suas próprias tarefas

            stmt.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar tarefa: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void remover(int idTarefa, int idUsuario) {
        String sql = "DELETE FROM tarefas WHERE id = ? AND id_usuario = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTarefa);
            stmt.setInt(2, idUsuario); // Garante que o usuário só possa deletar suas próprias tarefas

            stmt.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao remover tarefa: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

