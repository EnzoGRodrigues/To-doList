package view;

import dao.TarefaDAO;
import model.StatusTarefa;
import model.Tarefa;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class TelaCadastroEdicao extends JDialog {

    // --- Componentes ---
    private JTextField txtTitulo;
    private JTextArea txtDescricao;
    private JComboBox<String> comboStatus;
    private JTextField txtDataVencimento;
    private JButton btnSalvar;
    private JButton btnCancelar;

    // --- Lógica e Controle ---
    private TarefaDAO tarefaDAO;
    private Usuario usuarioLogado;
    private TelaPrincipal telaPrincipal; // Referência para a tela principal (para atualizar a lista)
    private Tarefa tarefaParaEditar; // Se for 'null', é cadastro. Se não, é edição.

    /**
     * Construtor para MODO CADASTRO (Nova Tarefa)
     */
    public TelaCadastroEdicao(TelaPrincipal telaPrincipal, Usuario usuarioLogado) {
        // 'super(telaPrincipal, true)' torna este JDialog modal em relação à tela principal
        super(telaPrincipal, "Nova Tarefa", true);

        this.telaPrincipal = telaPrincipal;
        this.usuarioLogado = usuarioLogado;
        this.tarefaDAO = new TarefaDAO();
        this.tarefaParaEditar = null; // Indica modo CADASTRO

        inicializarComponentes();
        adicionarListeners();
    }

    /**
     * Construtor para MODO EDIÇÃO (Editar Tarefa)
     */
    public TelaCadastroEdicao(TelaPrincipal telaPrincipal, Usuario usuarioLogado, Tarefa tarefaParaEditar) {
        super(telaPrincipal, "Editar Tarefa", true);

        this.telaPrincipal = telaPrincipal;
        this.usuarioLogado = usuarioLogado;
        this.tarefaDAO = new TarefaDAO();
        this.tarefaParaEditar = tarefaParaEditar; // Indica modo EDIÇÃO

        inicializarComponentes();
        preencherCampos(); // Preenche os campos com os dados da tarefa
        adicionarListeners();
    }

    /**
     * Cria e organiza os componentes visuais na tela.
     */
    private void inicializarComponentes() {
        setSize(450, 400);
        setLocationRelativeTo(telaPrincipal); // Centraliza em relação à tela principal
        setLayout(new BorderLayout(10, 10));

        // --- Painel do Formulário (Centro) ---
        JPanel painelFormulario = new JPanel();
        painelFormulario.setLayout(new BoxLayout(painelFormulario, BoxLayout.Y_AXIS));
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Campo Título
        painelFormulario.add(new JLabel("Título:"));
        txtTitulo = new JTextField(30);
        txtTitulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtTitulo.getPreferredSize().height)); // Evita esticar
        painelFormulario.add(txtTitulo);
        painelFormulario.add(Box.createRigidArea(new Dimension(0, 10))); // Espaçamento

        // Campo Descrição
        painelFormulario.add(new JLabel("Descrição:"));
        txtDescricao = new JTextArea(5, 30);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        painelFormulario.add(scrollDescricao);
        painelFormulario.add(Box.createRigidArea(new Dimension(0, 10)));

        // Linha para Status e Data (lado a lado)
        JPanel painelStatusData = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        // Campo Status
        JPanel painelStatus = new JPanel();
        painelStatus.setLayout(new BoxLayout(painelStatus, BoxLayout.Y_AXIS));
        painelStatus.add(new JLabel("Status:"));
        String[] statusOpcoes = {StatusTarefa.PENDENTE.getValorSql(),
                StatusTarefa.EM_ANDAMENTO.getValorSql(),
                StatusTarefa.CONCLUIDA.getValorSql()};
        comboStatus = new JComboBox<>(statusOpcoes);
        painelStatus.add(comboStatus);

        // Campo Data
        JPanel painelData = new JPanel();
        painelData.setLayout(new BoxLayout(painelData, BoxLayout.Y_AXIS));
        painelData.add(new JLabel("Data Vencimento (yyyy-mm-dd):"));
        txtDataVencimento = new JTextField(12);
        painelData.add(txtDataVencimento);

        painelStatusData.add(painelStatus);
        painelStatusData.add(Box.createRigidArea(new Dimension(15, 0))); // Espaço entre eles
        painelStatusData.add(painelData);

        painelFormulario.add(painelStatusData);

        // --- Painel de Botões (Sul) ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        // Adiciona os painéis principais à janela
        add(painelFormulario, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    /**
     * Preenche os campos com os dados da tarefa (usado no modo Edição).
     */
    private void preencherCampos() {
        if (tarefaParaEditar != null) {
            txtTitulo.setText(tarefaParaEditar.getTitulo());
            txtDescricao.setText(tarefaParaEditar.getDescricao());
            comboStatus.setSelectedItem(tarefaParaEditar.getStatus().getValorSql());
            if (tarefaParaEditar.getDataVencimento() != null) {
                txtDataVencimento.setText(tarefaParaEditar.getDataVencimento().toString());
            }
        }
    }

    /**
     * Adiciona a lógica aos botões.
     */
    private void adicionarListeners() {
        // Ação do Botão Salvar
        btnSalvar.addActionListener(e -> salvarTarefa());

        // Ação do Botão Cancelar
        btnCancelar.addActionListener(e -> dispose()); // 'dispose()' fecha o JDialog
    }

    /**
     * Lógica principal: valida os dados e chama o DAO para salvar (ou criar ou atualizar).
     */
    private void salvarTarefa() {
        // 1. Coletar dados da tela
        String titulo = txtTitulo.getText();
        String descricao = txtDescricao.getText();
        String statusString = (String) comboStatus.getSelectedItem();
        StatusTarefa status = StatusTarefa.fromString(statusString); // Converte String para Enum
        String dataString = txtDataVencimento.getText();

        // 2. Validação simples
        if (titulo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O título é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Validação/Conversão da Data
        LocalDate dataVencimento = null;
        if (!dataString.trim().isEmpty()) {
            try {
                dataVencimento = LocalDate.parse(dataString); // Formato yyyy-mm-dd
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de data inválido. Use yyyy-mm-dd.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try {
            // 4. Decide se é CADASTRO (INSERT) ou ATUALIZAÇÃO (UPDATE)
            if (tarefaParaEditar == null) {
                // --- MODO CADASTRO ---
                Tarefa novaTarefa = new Tarefa();
                novaTarefa.setTitulo(titulo);
                novaTarefa.setDescricao(descricao);
                novaTarefa.setStatus(status);
                novaTarefa.setDataVencimento(dataVencimento);
                novaTarefa.setIdUsuario(usuarioLogado.getId()); // Vínculo com o usuário!

                tarefaDAO.inserir(novaTarefa);
                JOptionPane.showMessageDialog(this, "Tarefa cadastrada com sucesso!");

            } else {
                // --- MODO EDIÇÃO ---
                tarefaParaEditar.setTitulo(titulo);
                tarefaParaEditar.setDescricao(descricao);

                // Lógica para data de conclusão: se o status MUDOU para "Concluída"
                if (status == StatusTarefa.CONCLUIDA && tarefaParaEditar.getStatus() != StatusTarefa.CONCLUIDA) {
                    tarefaParaEditar.setDataConclusao(LocalDateTime.now());
                }
                tarefaParaEditar.setStatus(status);
                tarefaParaEditar.setDataVencimento(dataVencimento);

                tarefaDAO.atualizar(tarefaParaEditar);
                JOptionPane.showMessageDialog(this, "Tarefa atualizada com sucesso!");
            }

            // 5. Se deu tudo certo: Atualiza a tabela principal e fecha o pop-up
            telaPrincipal.carregarTarefas(); // O método mágico de atualização!
            dispose(); // Fecha esta janela

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
