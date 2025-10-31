package testes;

import dao.TarefaDAO;
import dao.UsuarioDAO;
import model.StatusTarefa;
import model.Tarefa;
import model.Usuario;

import java.time.LocalDate;
import java.util.List;

public class Testes {
    public static void main(String[] args) {
        // --- PRÉ-REQUISITOS ---
        // 1. Tenha certeza que seu servidor MySQL está rodando.
        // 2. Tenha certeza que você já executou o script SQL para criar o banco "todo_list_db" e as tabelas.

        System.out.println("--- INICIANDO TESTES DO BACKEND ---");

        // Instancia os DAOs que vamos testar
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        TarefaDAO tarefaDAO = new TarefaDAO();

        // ** TESTE 1: CADASTRAR NOVO USUÁRIO
        System.out.println("Executando Teste 1: Cadastrar Usuário...");
        Usuario novoUsuario = new Usuario("usuario.teste6", "senha000");
        usuarioDAO.cadastrar(novoUsuario);
        // Você verá um JOptionPane de "sucesso"
        System.out.println("Usuário 'usuario.teste' cadastrado.");


        // ** TESTE 2: AUTENTICAR USUÁRIO (SUCESSO)
        System.out.println("\nExecutando Teste 2: Autenticar Usuário (Sucesso)...");
        Usuario usuarioLogado = usuarioDAO.autenticar("usuario.teste6", "senha000");

        if (usuarioLogado != null) {
            System.out.println("Sucesso! Usuário autenticado: " + usuarioLogado.getLogin());
            System.out.println("ID do usuário logado: " + usuarioLogado.getId());
        } else {
            System.out.println("FALHA NO TESTE! Autenticação deveria ter funcionado.");
            return; // Para os testes se a autenticação falhar
        }


//        // ** TESTE 3: AUTENTICAR USUÁRIO (FALHA) *
//        System.out.println("\nExecutando Teste 3: Autenticar Usuário (Falha)...");
//        Usuario usuarioFalho = usuarioDAO.autenticar("usuario.teste", "senhaERRADA");
//        if (usuarioFalho == null) {
//            System.out.println("Sucesso! Login com senha errada falhou, como esperado.");
//        } else {
//            System.out.println("FALHA NO TESTE! Autenticação deveria ter falhado.");
//        }


        // ** TESTE 4: INSERIR TAREFA PARA O USUÁRIO LOGADO
        System.out.println("\nExecutando Teste 4: Inserir Tarefa...");
        Tarefa novaTarefa = new Tarefa();
        novaTarefa.setTitulo("Testar o DAO");
        novaTarefa.setDescricao("Fazer o teste do método main funcionar.");
        novaTarefa.setStatus(StatusTarefa.PENDENTE); // Use seu Enum
        novaTarefa.setDataVencimento(LocalDate.now().plusDays(5)); // Vence em 5 dias
        novaTarefa.setIdUsuario(usuarioLogado.getId()); // USA O ID DO USUÁRIO LOGADO!

        tarefaDAO.inserir(novaTarefa);
        System.out.println("Tarefa inserida com sucesso!");


        // ** TESTE 5: LISTAR TAREFAS DO USUÁRIO
        System.out.println("\nExecutando Teste 5: Listar Tarefas...");
        List<Tarefa> tarefas = tarefaDAO.listarPorUsuario(usuarioLogado.getId());
        if (tarefas.isEmpty()) {
            System.out.println("FALHA NO TESTE! A lista de tarefas não deveria estar vazia.");
        } else {
            System.out.println("Sucesso! Tarefas encontradas: " + tarefas.size());
            System.out.println(tarefas.get(0).toString()); // Imprime a primeira tarefa
        }


        // TESTE 6: ATUALIZAR TAREFA
        System.out.println("\nExecutando Teste 6: Atualizar Tarefa...");
        // Pega a primeira tarefa da lista para atualizar
        Tarefa tarefaParaAtualizar = tarefas.get(0);
        tarefaParaAtualizar.setStatus(StatusTarefa.CONCLUIDA); // Muda o status
        tarefaParaAtualizar.setDescricao("Descrição foi atualizada.");

        tarefaDAO.atualizar(tarefaParaAtualizar);
        System.out.println("Tarefa atualizada com sucesso!");


        // TESTE 7: LISTAR NOVAMENTE PARA VER A ATUALIZAÇÃO
        System.out.println("\nExecutando Teste 7: Listar Novamente...");
        List<Tarefa> tarefasAtualizadas = tarefaDAO.listarPorUsuario(usuarioLogado.getId());
        Tarefa tarefaAtualizada = tarefasAtualizadas.get(0);
        System.out.println("Status novo: " + tarefaAtualizada.getStatus()); // Deve ser EM_ANDAMENTO
        System.out.println("Descrição nova: " + tarefaAtualizada.getDescricao());
//
//
//        // TESTE 8: REMOVER TAREFA
//        System.out.println("\nExecutando Teste 8: Remover Tarefa...");
//        tarefaDAO.remover(tarefaAtualizada.getId(), usuarioLogado.getId());
//        System.out.println("Tarefa removida com sucesso!");
//
//
        // TESTE 9: LISTAR FINAL
        System.out.println("\nExecutando Teste 9: Listar Final...");
        List<Tarefa> tarefasFinais = tarefaDAO.listarPorUsuario(usuarioLogado.getId());
        if (tarefasFinais.isEmpty()) {
            System.out.println("Sucesso! Tarefa foi removida e a lista está vazia.");
        } else {
            System.out.println("FALHA NO TESTE! A tarefa não foi removida.");
        }


        System.out.println("\n--- TESTES DO BACKEND CONCLUÍDOS ---");
    }
}

