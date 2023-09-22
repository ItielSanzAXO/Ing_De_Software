/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package calculodebarras;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author ItielSanz<ItielSanzAXO>
 */
public class CalculoDeBarras {

    public static void main(String[] args) {
        // Obtén una instancia de DatabaseConnection con la cadena de conexión adecuada
        String connectionString = "jdbc:sqlserver://ingsoftdatabase.database.windows.net:1433;database=IngSoftwareDB;user=admin26@ingsoftdatabase;password=@Axopunk2023.;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
        DatabaseConnection dbConnection = DatabaseConnection.getInstance(connectionString);

        // Declara la conexión fuera del bloque try-with-resources
        Connection connection = null;

        // Intenta obtener la conexión a la base de datos
        connection = dbConnection.getConnection();
        // Verifica si la conexión se estableció con éxito
        if (connection != null) {
            System.out.println("Conexión exitosa a la base de datos.");
            
            Scanner lectura = new Scanner (System.in);            
            // Aquí puedes realizar operaciones en la base de datos
            // Por ejemplo, ejecutar consultas o realizar operaciones de base de datos
            
            // No cierres la conexión aquí, mantenla abierta para realizar más operaciones
            
        } else {
            System.err.println("Error al conectar a la base de datos.");
        }
        // Cierra la conexión aquí cuando ya no la necesites
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
