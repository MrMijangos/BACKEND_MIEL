package org.API_Miel.ConfigDB;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;
    
    // Bandera para saber si ya se inicializó
    private static boolean isInitialized = false; 
    
    public static void initialize() {
        if (isInitialized) return; // Evitar reinicializar si ya está listo

        try {
            // Cargar .env (ignorar si falta el archivo para usar valores por defecto)
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            
            // Obtener variables con lógica manual de "valor por defecto"
            String hostEnv = dotenv.get("DB_HOST");
            String host = (hostEnv != null) ? hostEnv : "localhost";

            String portEnv = dotenv.get("DB_PORT");
            String port = (portEnv != null) ? portEnv : "3306";

            String dbNameEnv = dotenv.get("DB_NAME");
            String database = (dbNameEnv != null) ? dbNameEnv : "ecommerce_miel";
            
            String userEnv = dotenv.get("DB_USER");
            DB_USER = (userEnv != null) ? userEnv : "root";

            String passEnv = dotenv.get("DB_PASSWORD");
            DB_PASSWORD = (passEnv != null) ? passEnv : "";
            
            // Construir URL
            DB_URL = "jdbc:mysql://" + host + ":" + port + "/" + database 
                   + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            
            // Cargar driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            isInitialized = true;
            System.out.println("✅ Configuración de base de datos inicializada");
            System.out.println("🔗 URL: " + DB_URL); // Útil para depurar (quitar en producción)
            
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error al cargar el driver de MySQL", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        // Aseguramos que se haya ejecutado initialize() antes de pedir una conexión
        if (!isInitialized) {
            initialize();
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}