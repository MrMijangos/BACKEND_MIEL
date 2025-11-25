package org.API_Miel.Routers;

import io.javalin.Javalin;
import org.API_Miel.ConfigDB.ConfigDB;
import org.API_Miel.Controllers.*;
import com.password4j.Password;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

public class ApiRouters {
    public static void register(Javalin app) {
        Authcontroller auth = new Authcontroller();
        ProductController products = new ProductController();
        CartController cart = new CartController();
        OrderController orders = new OrderController();
        ShippingController shipping = new ShippingController();
        PaymentController payment = new PaymentController();
        CommentController comments = new CommentController();

        app.post("/api/auth/register", auth::register);
        app.post("/api/auth/login", auth::login);

        app.get("/health", ctx -> ctx.json(Map.of("status", "ok")));

        app.get("/api/products", products::list);
        app.get("/api/products/{id}", products::get);
        app.post("/api/products", products::create);
        app.put("/api/products/{id}", products::update);
        app.delete("/api/products/{id}", products::delete);
        app.get("/api/charts/products-stock", products::listStockChart);
        app.get("/api/products/top-selling", products::topSelling);
        // LO DE LA GRAFICA DE STOCK DE PRODUCTOS

        app.post("/api/cart/items", cart::addItem);
        app.delete("/api/cart/items/{idProducto}", cart::removeItem);
        app.get("/api/cart/items", cart::getItems);
        app.delete("/api/cart/clear", cart::clear);

        app.post("/api/orders", orders::create);
        app.get("/api/orders", orders::list);
        app.put("/api/orders/{id}/status", orders::updateStatus);

        app.get("/api/shipping-address", shipping::list);
        app.post("/api/shipping-address", shipping::create);
        app.delete("/api/shipping-address/{id}", shipping::delete);

        app.get("/api/payments", payment::list);
        app.post("/api/payments", payment::create);
        app.delete("/api/payments/{id}", payment::delete);

        app.get("/api/comments/{idProducto}", comments::listByProduct);
        app.post("/api/comments", comments::create);
        app.delete("/api/comments/{idResena}", comments::delete);

        app.post("/api/create-admin", ctx -> {
            try {
                Connection conn = ConfigDB.getDataSource().getConnection();

                PreparedStatement check = conn.prepareStatement("SELECT ID_Usuario FROM Usuario WHERE Correo = ?");
                check.setString(1, "admin@miel.com");
                var rs = check.executeQuery();

                if (rs.next()) {
                    conn.close();
                    ctx.json(Map.of("message", "Admin ya existe"));
                    return;
                }

                String hash = Password.hash("admin123").withBcrypt().getResult();

                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO Usuario (Nombre_Completo, Correo, Contrasena, Num_Celular, ID_Rol) VALUES (?, ?, ?, ?, ?)");
                stmt.setString(1, "Administrador");
                stmt.setString(2, "admin@miel.com");
                stmt.setString(3, hash);
                stmt.setString(4, "9611234567");
                stmt.setInt(5, 1);

                stmt.executeUpdate();
                conn.close();

                ctx.json(Map.of(
                        "message", "Admin creado exitosamente",
                        "hash_utilizado", hash,
                        "credenciales", Map.of(
                                "correo", "admin@miel.com",
                                "contrasenia", "admin123",
                                "rol", "Administrador")));
            } catch (Exception e) {
                ctx.status(500).json(Map.of("error", e.getMessage()));
            }
        });
    }
}
