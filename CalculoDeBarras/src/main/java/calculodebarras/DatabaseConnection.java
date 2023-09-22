package calculodebarras;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance = null;
    private Connection connection;

    // Constructor privado para evitar la creación de instancias desde fuera de la clase
    private DatabaseConnection(String connectionString) {
        try {
            connection = DriverManager.getConnection(connectionString);
            System.out.println("Conexión establecida a la base de datos.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener la única instancia de DatabaseConnection
    public static synchronized DatabaseConnection getInstance(String connectionString) {
        if (instance == null) {
            instance = new DatabaseConnection(connectionString);
        }
        return instance;
    }

    // Método para obtener la conexión a la base de datos sin cerrarla aquí
    public Connection getConnection() {
        return connection;
    }
}

