package org.API_Miel.ConfigDB;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConfigDB {
    private static HikariDataSource dataSource;

    public static DataSource getDataSource(){
        if(dataSource == null){
            Dotenv dotenv = Dotenv.configure().directory("./").load();
            String host = dotenv.get("DB_HOST");
            String port = dotenv.get("DB_PORT");
            String dbName = dotenv.get("DB_NAME");
            String user = dotenv.get("DB_USER");
            String password = dotenv.get("DB_PASS");
            String chosenUrl = null;
            String[] ports = new String[] { port, "3306" };
            for (String p : ports) {
                String base = "jdbc:mysql://" + host + ":" + p;
                try (Connection c = DriverManager.getConnection(base, user, password);
                     Statement s = c.createStatement()) {
                    s.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + dbName + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci");
                    chosenUrl = base + "/" + dbName;
                    break;
                } catch (SQLException ignored) {}
            }
            if (chosenUrl == null) throw new RuntimeException("No se pudo conectar a MySQL en " + host + ":" + port + " ni en puerto 3306");

            HikariConfig conf = new HikariConfig();
            conf.setJdbcUrl(chosenUrl);
            conf.setUsername(user);
            conf.setPassword(password);
            conf.setDriverClassName("com.mysql.cj.jdbc.Driver");

            dataSource = new HikariDataSource(conf);

            try (Connection c = dataSource.getConnection(); Statement s = c.createStatement()) {
                s.executeUpdate("CREATE TABLE IF NOT EXISTS usuario (\n" +
                        "  ID_Usuario BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
                        "  Nombre_Completo VARCHAR(255) NOT NULL,\n" +
                        "  Correo VARCHAR(255) NOT NULL UNIQUE,\n" +
                        "  Contrasena VARCHAR(255) NOT NULL,\n" +
                        "  Num_Celular VARCHAR(50),\n" +
                        "  ID_Rol INT NOT NULL\n" +
                        ") ENGINE=InnoDB");

                s.executeUpdate("CREATE TABLE IF NOT EXISTS producto (\n" +
                        "  ID_Producto BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
                        "  Nombre VARCHAR(255) NOT NULL,\n" +
                        "  Descripcion TEXT,\n" +
                        "  Precio DECIMAL(10,2) NOT NULL,\n" +
                        "  Stock INT NOT NULL,\n" +
                        "  Imagen TEXT\n" +
                        ") ENGINE=InnoDB");

                s.executeUpdate("CREATE TABLE IF NOT EXISTS carrito (\n" +
                        "  ID_Carrito BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
                        "  ID_Usuario BIGINT NOT NULL,\n" +
                        "  FOREIGN KEY (ID_Usuario) REFERENCES usuario(ID_Usuario) ON DELETE CASCADE\n" +
                        ") ENGINE=InnoDB");

                s.executeUpdate("CREATE TABLE IF NOT EXISTS carrito_detalle (\n" +
                        "  ID_Detalle BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
                        "  ID_Carrito BIGINT NOT NULL,\n" +
                        "  ID_Producto BIGINT NOT NULL,\n" +
                        "  Cantidad INT NOT NULL,\n" +
                        "  UNIQUE KEY uk_carrito_producto (ID_Carrito, ID_Producto),\n" +
                        "  FOREIGN KEY (ID_Carrito) REFERENCES carrito(ID_Carrito) ON DELETE CASCADE,\n" +
                        "  FOREIGN KEY (ID_Producto) REFERENCES producto(ID_Producto)\n" +
                        ") ENGINE=InnoDB");

                s.executeUpdate("CREATE TABLE IF NOT EXISTS direccion (\n" +
                        "  ID_Direccion BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
                        "  ID_Usuario BIGINT NOT NULL,\n" +
                        "  Calle VARCHAR(255) NOT NULL,\n" +
                        "  Colonia VARCHAR(255),\n" +
                        "  Codigo_Postal VARCHAR(20),\n" +
                        "  Ciudad VARCHAR(100),\n" +
                        "  Estado VARCHAR(100),\n" +
                        "  FOREIGN KEY (ID_Usuario) REFERENCES usuario(ID_Usuario) ON DELETE CASCADE\n" +
                        ") ENGINE=InnoDB");

                s.executeUpdate("CREATE TABLE IF NOT EXISTS metodo_pago (\n" +
                        "  ID_Metodo_Pago BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
                        "  ID_Usuario BIGINT NOT NULL,\n" +
                        "  Tipo VARCHAR(100) NOT NULL,\n" +
                        "  Detalles TEXT,\n" +
                        "  FOREIGN KEY (ID_Usuario) REFERENCES usuario(ID_Usuario) ON DELETE CASCADE\n" +
                        ") ENGINE=InnoDB");

                s.executeUpdate("CREATE TABLE IF NOT EXISTS pedido (\n" +
                        "  ID_Pedido BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
                        "  Numero_Pedido VARCHAR(100) NOT NULL,\n" +
                        "  ID_Usuario BIGINT NOT NULL,\n" +
                        "  ID_Metodo_Pago BIGINT NOT NULL,\n" +
                        "  ID_Direccion BIGINT NOT NULL,\n" +
                        "  Total DECIMAL(10,2) NOT NULL,\n" +
                        "  Estado VARCHAR(50) NOT NULL,\n" +
                        "  Fecha TIMESTAMP NOT NULL,\n" +
                        "  FOREIGN KEY (ID_Usuario) REFERENCES usuario(ID_Usuario),\n" +
                        "  FOREIGN KEY (ID_Metodo_Pago) REFERENCES metodo_pago(ID_Metodo_Pago),\n" +
                        "  FOREIGN KEY (ID_Direccion) REFERENCES direccion(ID_Direccion)\n" +
                        ") ENGINE=InnoDB");

                s.executeUpdate("CREATE TABLE IF NOT EXISTS pedido_detalle (\n" +
                        "  ID_Detalle BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
                        "  ID_Pedido BIGINT NOT NULL,\n" +
                        "  ID_Producto BIGINT NOT NULL,\n" +
                        "  Cantidad INT NOT NULL,\n" +
                        "  Subtotal DECIMAL(10,2) NOT NULL,\n" +
                        "  FOREIGN KEY (ID_Pedido) REFERENCES pedido(ID_Pedido) ON DELETE CASCADE,\n" +
                        "  FOREIGN KEY (ID_Producto) REFERENCES producto(ID_Producto)\n" +
                        ") ENGINE=InnoDB");

                s.executeUpdate("CREATE TABLE IF NOT EXISTS resena (\n" +
                        "  ID_Resena BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
                        "  ID_Producto BIGINT NOT NULL,\n" +
                        "  ID_Usuario BIGINT NOT NULL,\n" +
                        "  Calificacion INT NOT NULL,\n" +
                        "  Comentario TEXT,\n" +
                        "  Fecha TIMESTAMP NOT NULL,\n" +
                        "  FOREIGN KEY (ID_Producto) REFERENCES producto(ID_Producto),\n" +
                        "  FOREIGN KEY (ID_Usuario) REFERENCES usuario(ID_Usuario)\n" +
                        ") ENGINE=InnoDB");
            } catch (SQLException ignored) {}
        }
        return dataSource;
    }
}