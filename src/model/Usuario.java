package model;

public class Usuario {
    private int id;
    private String login;
    private String senha;

    public Usuario(){}

    // Segundo Construtor para criar um novo usuário (sem o id)
    // Usado quando você vai CADASTRAR um usuário novo, pois o ID ainda não existe.
    public Usuario(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    // Terceiro Construtor completo (para quando você LÊ do banco)
    // Usado quando você recupera um usuário existente do banco, pois ele já tem um ID.
    public Usuario(int id, String login, String senha) {
        this.id = id;
        this.login = login;
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", login='" + login + '\'' +
                '}';
    }
}
