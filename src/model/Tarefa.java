// MODEL - classes que representam dados

package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Tarefa {
    private int id;
    private String titulo;
    private String descricao;
    private StatusTarefa status;
    private LocalDateTime dataCriacao;
    private LocalDate dataVencimento; // Apenas data, sem hora
    private LocalDateTime dataConclusao; // Data e hora
    private int idUsuario; // Chave estrangeira para o usuário

    public Tarefa(){} // útil para quando você cria um objeto e só depois preenche com setters.

    public Tarefa(int id, String titulo, String descricao, StatusTarefa status, LocalDateTime dataCriacao,
                  LocalDate dataVencimento, LocalDateTime dataConclusao, int idUsuario) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataVencimento = dataVencimento;
        this.dataConclusao = dataConclusao;
        this.idUsuario = idUsuario;
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

    public StatusTarefa getStatus() {
        return status;
    }

    public void setStatus(StatusTarefa status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "Tarefa{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", status='" + status + '\'' +
                ", dataVencimento=" + dataVencimento +
                ", idUsuario=" + idUsuario +
                '}';
    }

}
