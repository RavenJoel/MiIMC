package com.joel.miimc.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Crea las conexiones JDBC a MySQL con los datos de db.properties.
 */
public class ConexionBD {

    private static final Logger LOGGER = Logger.getLogger(ConexionBD.class.getName());
    private static final Properties CONFIG = new Properties();

    static {
        try (InputStream in = ConexionBD.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("No se encontró db.properties");
            }
            CONFIG.load(in);
            // Registra el driver de MySQL (necesario dentro del servidor de aplicaciones)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public Connection obtenerConexion() {
        try {
            return DriverManager.getConnection(CONFIG.getProperty("db.url"),
                    CONFIG.getProperty("db.usuario"), CONFIG.getProperty("db.contrasena"));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "No fue posible conectar con MySQL", e);
            throw new AccesoDatosException("No fue posible conectar con la base de datos.", e);
        }
    }

    /** Prueba rápida de la conexión: clic derecho sobre esta clase → Run File. */
    public static void main(String[] args) throws SQLException {
        try (Connection con = new ConexionBD().obtenerConexion()) {
            System.out.println("Conexión exitosa a " + con.getCatalog()
                    + " (MySQL " + con.getMetaData().getDatabaseProductVersion() + ")");
        }
    }
}
