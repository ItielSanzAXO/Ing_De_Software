/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package calculodebarras;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author ItielSanz<ItielSanzAXO>
 */
public class CalculoDeBarras {

    public static void main(String[] args) {
        // Obtén una instancia de DatabaseConnection con la cadena de conexión adecuada
        String connectionString = "jdbc:sqlserver://ingsoftdatabase.database.windows.net:1433;database=IngSoftwareDB;user=admin26@ingsoftdatabase;password=@Axopunk2023.;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        DatabaseConnection dbConnection = DatabaseConnection.getInstance(connectionString);

        // Intenta obtener la conexión a la base de datos
        Connection connection = dbConnection.getConnection();

        // Verifica si la conexión se estableció con éxito
        if (connection != null) {
            System.out.println("Conexión exitosa a la base de datos.");
            
            // Aquí puedes realizar operaciones en la base de datos
            
            // No olvides cerrar la conexión cuando hayas terminado
            try {
                connection.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Error al conectar a la base de datos.");
        }
    }
}
