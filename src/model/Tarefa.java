package model;

public class Tarefa {
    private int  id;
    private String titulo;
    private String descricao;
    private String status;
    private String data_criacao;
    private String data_conclusao;

    public Tarefa(){}

    public Tarefa(String titulo, String descricao, String stauts){
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = stauts;
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
}
