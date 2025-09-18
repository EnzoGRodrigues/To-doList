import util.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            Connection conn = connectionFactory.getConnection();
            System.out.println("Conexão feita com sucesso! ");
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}