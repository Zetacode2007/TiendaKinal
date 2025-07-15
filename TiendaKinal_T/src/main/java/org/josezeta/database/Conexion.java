
package org.josezeta.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author RV
 */
public class Conexion {
    //singleton
    private static Conexion instancia;
    //Connection
    private Connection conexion;
    //Cadena de conexion
     private String url = "jdbc:mysql://127.0.0.1:3306/DBVeterinaria?useSSL=false";
     private String user = "root";
     private String password = "root";
     private String driver = "com.mysql.jdbc.Driver";
     //Constructor
     private Conexion(){
         conectar();
     }
    //Configurar conexion a MySQL
    public void conectar(){
        try {
            Class.forName(driver);
            conexion = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al conectar:"+e.getMessage());
        }
    }
    //getInstancia,
    public static Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }
    //getConexion,
    public Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                conectar();
            }
        } catch (SQLException e) {
            System.out.println("Error al reconectar: "+e.getSQLState());
        }
        return conexion;
    }
    //desconectar

}
