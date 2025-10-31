package view;

import dao.UsuarioDAO;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaLogin extends JFrame {

    // Componentes da tela
    private JTextField txtLogin;
    private JPasswordField txtSenha; // Componente específico para senhas
    private JButton btnEntrar;
    private JButton btnCadastrar;

    // DAO que será usado pela tela
    private UsuarioDAO usuarioDAO;

    // Construtor: é aqui que a tela é "montada"
    public TelaLogin() {
        // Inicializa o DAO
        this.usuarioDAO = new UsuarioDAO();

        // Configurações básicas da Janela (JFrame)
        setTitle("Login - Sistema de Tarefas");
        setSize(400, 200); // Define um tamanho
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Encerra a aplicação ao fechar
        setLocationRelativeTo(null); // Centraliza a janela na tela
        setLayout(new BorderLayout(10, 10)); // Layout principal com espaçamento

        // Painel Central para os campos (Formulário)
        JPanel painelFormulario = new JPanel(new GridLayout(2, 2, 10, 10)); // 2 linhas, 2 colunas
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margem

        // Campo Login
        painelFormulario.add(new JLabel("Login:"));
        txtLogin = new JTextField();
        painelFormulario.add(txtLogin);

        // Campo Senha
        painelFormulario.add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        painelFormulario.add(txtSenha);

        // Adiciona o painel de formulário ao centro da janela
        add(painelFormulario, BorderLayout.CENTER);

        // Painel Inferior para os Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Layout fluido

        btnEntrar = new JButton("Entrar");
        painelBotoes.add(btnEntrar);

        btnCadastrar = new JButton("Cadastrar-se");
        painelBotoes.add(btnCadastrar);

        // Adiciona o painel de botões ao sul da janela
        add(painelBotoes, BorderLayout.SOUTH);

        // Ação do Botão "Entrar"
        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chama o métod.o de login
                logar();
            }
        });

        // Ação do Botão "Cadastrar-se"
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chama o métod.o de cadastro
                cadastrarNovoUsuario();
            }
        });
    }

    // Métodos de Ação

    private void logar() {
        // Pegar os dados da tela
        String login = txtLogin.getText();
        String senha = new String(txtSenha.getPassword()); // Pega a senha do JPasswordField

        // Validar campos (simples)
        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Login e Senha são obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Chamar o DAO para autenticar
        Usuario usuarioLogado = usuarioDAO.autenticar(login, senha);

        // Tratar o resultado
        if (usuarioLogado != null) {
            // Sucesso!
            JOptionPane.showMessageDialog(this, "Login bem-sucedido! Bem-vindo, " + usuarioLogado.getLogin(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);


            new TelaPrincipal(usuarioLogado).setVisible(true);


            // Fecha a tela de login
            this.dispose();

        } else {
            // O DAO já exibe a mensagem de "Login ou senha inválidos."
            // Não precisamos fazer nada aqui, pois o DAO.autenticate() já tratou o erro.
        }
    }

    private void cadastrarNovoUsuario() {
        // Para um cadastro simples, vamos usar pop-ups (JOptionPane)
        String login = JOptionPane.showInputDialog(this, "Digite seu novo login:", "Cadastro", JOptionPane.PLAIN_MESSAGE);

        // Verifica se o usuário cancelou
        if (login == null || login.trim().isEmpty()) {
            return; // Sai do métod.o se o login for vazio ou cancelado
        }

        String senha = JOptionPane.showInputDialog(this, "Digite sua nova senha:", "Cadastro", JOptionPane.PLAIN_MESSAGE);

        if (senha == null || senha.trim().isEmpty()) {
            return; // Sai do métod.o se a senha for vazia ou cancelada
        }

        // Cria o novo usuário e chama o DAO
        Usuario novoUsuario = new Usuario(login, senha);
        usuarioDAO.cadastrar(novoUsuario);
        // O DAO.cadastrar() já exibe a mensagem de "Usuário cadastrado com sucesso!"
    }


    // Métod.o Main (para testar só esta tela) ---
    public static void main(String[] args) {
        // Isso garante que a interface rode na "thread" correta de eventos do Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Tenta aplicar o "look and feel" do sistema operacional
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Cria e exibe a tela de login
                new TelaLogin().setVisible(true);
            }
        });
    }
}
