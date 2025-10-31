package dao;

import model.Usuario;
import util.ConnectionFactory;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public void cadastrar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (login, senha) VALUES (?, ?)";

        // Usando try-with-resources para garantir que a conexão e o statement sejam fechados
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            // Seta os valores dos parâmetros (?) do SQL
            ps.setString(1, usuario.getLogin());
            ps.setString(2, usuario.getSenha());

            // Executa o comando de inserção
            ps.executeUpdate();

            // Feedback para o usuário (opcional, mas bom para a interface)
            JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");

        } catch (SQLException e) {
            // Tratamento de exceção
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Usuario autenticar(String login, String senha) {
        String sql = "SELECT id, login FROM usuarios WHERE login = ? AND senha = ?";

        // Usando try-with-resources
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            // Seta os parâmetros da consulta
            ps.setString(1, login);
            ps.setString(2, senha);

            // Executa a consulta
            try (ResultSet rs = ps.executeQuery()) {

                // Verifica se encontrou um resultado
                if (rs.next()) {
                    // Se encontrou, cria um objeto Usuario com os dados do banco
                    int id = rs.getInt("id");
                    String loginUsuario = rs.getString("login");

                    // Retorna o objeto Usuario (sem a senha, por segurança)
                    return new Usuario(id, loginUsuario, null); // Passa null para a senha
                } else {
                    // Se não encontrou, as credenciais estão erradas
                    JOptionPane.showMessageDialog(null, "Login ou senha inválidos.");
                    return null;
                }
            }
        } catch (SQLException e) {
            // Tratamento de exceção
            JOptionPane.showMessageDialog(null, "Erro ao autenticar: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
