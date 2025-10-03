// MODEL - classes que representam dados

package model;

import java.time.LocalDateTime;

public class Tarefa {
    private int  id;
    private String titulo;
    private String descricao;
    private String status;
    private LocalDateTime data_criacao;
    private LocalDateTime data_conclusao;

    public Tarefa(){} // útil para quando você cria um objeto e só depois preenche com setters.

    public Tarefa(String titulo, String descricao, String stauts, LocalDateTime data_criacao, LocalDateTime data_conclusao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = stauts;
        this.data_criacao = data_criacao;
        this.data_conclusao = data_conclusao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(LocalDateTime data_criacao) {
        this.data_criacao = data_criacao;
    }

    public LocalDateTime getData_conclusao() {
        return data_conclusao;
    }

    public void setData_conclusao(LocalDateTime data_conclusao) {
        this.data_conclusao = data_conclusao;
    }
}
