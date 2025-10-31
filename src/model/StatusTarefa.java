package model;

public enum StatusTarefa {

    // 1. Definição dos Enums e seus valores "reais" do banco
    PENDENTE("Pendente"),
    EM_ANDAMENTO("Em andamento"),
    CONCLUIDA("Concluída"); // Cuidado com o acento!

    // 2. Atributo para guardar o valor do banco
    private final String valorSql;

    // 3. Construtor do Enum
    StatusTarefa(String valorSql) {
        this.valorSql = valorSql;
    }

    // 4. Método para "pegar" o valor do banco (para INSERIR)
    public String getValorSql() {
        return valorSql;
    }

    // 5. Método para "traduzir" do banco para o Enum (para LER)
    public static StatusTarefa fromString(String texto) {
        if (texto == null) {
            return null;
        }

        // Itera sobre todos os valores do Enum (PENDENTE, EM_ANDAMENTO, CONCLUIDA)
        for (StatusTarefa s : StatusTarefa.values()) {
            // Compara o texto do banco (ex: "Em andamento") com o valorSql do Enum
            if (s.valorSql.equalsIgnoreCase(texto)) {
                return s; // Retorna o Enum StatusTarefa.EM_ANDAMENTO
            }
        }

        // Se não encontrar, lança um erro
        throw new IllegalArgumentException("Nenhum status encontrado para a string: " + texto);
    }
}

