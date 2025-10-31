package view;

import dao.TarefaDAO;
import model.StatusTarefa;
import model.Tarefa;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TelaPrincipal extends JFrame {

    // --- Componentes da Tela ---
    private JTable tabelaTarefas;
    private JButton btnNovaTarefa;
    private JButton btnEditar;
    private JButton btnExcluir;
    private JComboBox<String> comboFiltroStatus;
    private JLabel lblBoasVindas;
    private DefaultTableModel tableModel; // O modelo de tabela padrão

    // --- Lógica e Dados ---
    private TarefaDAO tarefaDAO;
    private Usuario usuarioLogado;

    // IMPORTANTE: Esta lista manterá os objetos Tarefa em sincronia com as linhas da tabela.
    private List<Tarefa> tarefasNaTabela;

    // --- Construtor ---
    public TelaPrincipal(Usuario usuarioLogado) {
        // 1. Salvar o usuário logado e inicializar o DAO
        this.usuarioLogado = usuarioLogado;
        this.tarefaDAO = new TarefaDAO();
        this.tarefasNaTabela = new ArrayList<>(); // Inicializa a lista

        // 2. Configurações da Janela
        setTitle("Minhas Tarefas - " + usuarioLogado.getLogin());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 3. Montar a tela (chamar métodos auxiliares)
        criarPainelSuperior();
        criarPainelCentral(); // Este método vai mudar
        criarPainelLateral();

        // 4. Adicionar os "ouvintes" (lógica dos botões)
        adicionarListeners();

        // 5. Carregar os dados iniciais do banco
        carregarTarefas();
    }

    // --- Métodos de Montagem da Interface ---

    private void criarPainelSuperior() {
        JPanel painelSuperior = new JPanel(new BorderLayout(10, 10));
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblBoasVindas = new JLabel("Bem-vindo, " + usuarioLogado.getLogin() + "!");
        lblBoasVindas.setFont(new Font("Arial", Font.BOLD, 16));
        painelSuperior.add(lblBoasVindas, BorderLayout.WEST);

        // Painel de Filtros
        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelFiltros.add(new JLabel("Filtar por Status:"));

        String[] filtros = {"Todos",
                StatusTarefa.PENDENTE.getValorSql(),
                StatusTarefa.EM_ANDAMENTO.getValorSql(),
                StatusTarefa.CONCLUIDA.getValorSql()};
        comboFiltroStatus = new JComboBox<>(filtros);
        painelFiltros.add(comboFiltroStatus);

        painelSuperior.add(painelFiltros, BorderLayout.EAST);
        add(painelSuperior, BorderLayout.NORTH);
    }

    private void criarPainelCentral() {
        // 1. Definir as colunas da tabela
        String[] colunas = {"Título", "Status", "Data de Vencimento"};

        // 2. Criar o DefaultTableModel (NÃO editável)
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna todas as células não editáveis
            }
        };

        // 3. Criar a JTable com o modelo
        tabelaTarefas = new JTable(tableModel);

        // 4. Configurações da tabela
        tabelaTarefas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaTarefas.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaTarefas.setRowHeight(25);
        tabelaTarefas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // IMPEDIR que o usuário reordene as colunas (quebraria nossa lógica)
        tabelaTarefas.getTableHeader().setReorderingAllowed(false);
        tabelaTarefas.setAutoCreateRowSorter(false); // Desativa a ordenação

        // 5. Colocar a tabela dentro de um painel de rolagem
        JScrollPane scrollPane = new JScrollPane(tabelaTarefas);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void criarPainelLateral() {
        JPanel painelBotoes = new JPanel(new GridLayout(3, 1, 10, 10));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));

        btnNovaTarefa = new JButton("Nova Tarefa");
        btnEditar = new JButton("Editar");
        btnExcluir = new JButton("Excluir");

        Dimension btnSize = new Dimension(120, 40);
        btnNovaTarefa.setPreferredSize(btnSize);
        btnEditar.setPreferredSize(btnSize);
        btnExcluir.setPreferredSize(btnSize);

        painelBotoes.add(btnNovaTarefa);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);

        add(painelBotoes, BorderLayout.EAST);
    }

    // --- Métodos de Lógica (Ação) ---

    private void adicionarListeners() {
        comboFiltroStatus.addActionListener(e -> carregarTarefas());
        btnNovaTarefa.addActionListener(e -> acaoNovaTarefa());
        btnEditar.addActionListener(e -> acaoEditar());
        btnExcluir.addActionListener(e -> acaoExcluir());
    }

    /**
     * Métod.o central! Busca os dados no DAO e atualiza a tabela.
     */
    public void carregarTarefas() {
        // 1. Pega o valor do filtro
        String filtroSelecionado = (String) comboFiltroStatus.getSelectedItem();

        // 2. Busca TODAS as tarefas do usuário no banco
        List<Tarefa> todasTarefas = tarefaDAO.listarPorUsuario(usuarioLogado.getId());

        // 3. Filtra a lista em Java
        if (filtroSelecionado.equals("Todos")) {
            this.tarefasNaTabela = todasTarefas; // Guarda a lista filtrada
        } else {
            this.tarefasNaTabela = todasTarefas.stream()
                    .filter(t -> t.getStatus().getValorSql().equals(filtroSelecionado))
                    .collect(Collectors.toList());
        }

        // 4. Limpar o modelo da tabela (remove linhas antigas)
        tableModel.setRowCount(0);

        // 5. Adicionar as tarefas filtradas ao modelo da tabela
        for (Tarefa tarefa : this.tarefasNaTabela) {
            Object[] linha = new Object[]{
                    tarefa.getTitulo(),
                    tarefa.getStatus().getValorSql(),
                    tarefa.getDataVencimento() // LocalDate formata bem
            };
            tableModel.addRow(linha);
        }
    }

    private void acaoNovaTarefa() {
        // TODO: Criar e chamar a TelaCadastroEdicao
        // O "this" é para a tela de cadastro poder chamar o "carregarTarefas()" de volta.
        // TelaCadastroEdicao telaCadastro = new TelaCadastroEdicao(this, usuarioLogado);
        // telaCadastro.setVisible(true);

        TelaCadastroEdicao telaCadastro = new TelaCadastroEdicao(this, usuarioLogado);
        telaCadastro.setVisible(true); // O programa vai "pausar" aqui até a tela fechar
    }

    private void acaoEditar() {
        int linhaSelecionada = tabelaTarefas.getSelectedRow();

        if (linhaSelecionada == -1) { // -1 significa que nenhuma linha foi selecionada
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma tarefa para editar.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // A MÁGICA SIMPLES: Pega o objeto Tarefa da nossa lista, usando o índice da linha
        Tarefa tarefaSelecionada = tarefasNaTabela.get(linhaSelecionada);

        // TODO: Criar e chamar a TelaCadastroEdicao, passando a tarefa
        // TelaCadastroEdicao telaEdicao = new TelaCadastroEdicao(this, usuarioLogado, tarefaSelecionada);
        // telaEdicao.setVisible(true);

        TelaCadastroEdicao telaEdicao = new TelaCadastroEdicao(this, usuarioLogado, tarefaSelecionada);
        telaEdicao.setVisible(true); // O programa vai "pausar" aqui
    }

    private void acaoExcluir() {
        int linhaSelecionada = tabelaTarefas.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma tarefa para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Pega a tarefa da nossa lista
        Tarefa tarefaParaExcluir = tarefasNaTabela.get(linhaSelecionada);

        // Pergunta de confirmação
        int resposta = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir a tarefa: \n" + tarefaParaExcluir.getTitulo(),
                "Confirmação de Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (resposta == JOptionPane.YES_OPTION) {
            // Chama o DAO para excluir
            tarefaDAO.remover(tarefaParaExcluir.getId(), usuarioLogado.getId());

            // Recarrega a lista
            carregarTarefas();
            JOptionPane.showMessageDialog(this, "Tarefa excluída com sucesso!");
        }
    }

    // --- Métod.o Main (Apenas para TESTAR esta tela isoladamente) ---
    public static void main(String[] args) {
        // Cria um usuário "fake" SÓ PARA TESTAR a tela
        // Lembre-se que o usuário 1 deve ter tarefas no banco para a lista aparecer
        Usuario usuarioTeste = new Usuario(1, "usuario.teste", null);

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new TelaPrincipal(usuarioTeste).setVisible(true);
        });
    }
}
