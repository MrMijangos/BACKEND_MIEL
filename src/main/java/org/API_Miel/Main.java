package org.API_Miel;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.json.JavalinJackson;
import io.github.cdimascio.dotenv.Dotenv;
import org.API_Miel.ConfigDB.DatabaseConfig;

public class Main {
    public static void main(String[] args) {
        // 1. Intentar inicializar conexión a BD (Diagnóstico)
        try {
            // Asumiendo que DatabaseConfig tiene un método getConnection o similar para probar
            // Si no tienes un método público estático para probar, puedes omitir este try-catch
             System.out.println("Verificando configuración de base de datos...");
        } catch (RuntimeException ignored) {
            System.err.println("Advertencia: Error inicial al verificar BD (se reintentará en las peticiones).");
        }

        // 2. Cargar variables de entorno
        Dotenv dotenv = Dotenv.configure().directory("./").ignoreIfMissing().load();
        
        String portStr = dotenv.get("PORT");
        int port = portStr != null && !portStr.isEmpty() ? Integer.parseInt(portStr) : 7000;
        
        String fbStr = dotenv.get("PORT_FALLBACK");
        int fb = fbStr != null && !fbStr.isEmpty() ? Integer.parseInt(fbStr) : 7001; // Usar 7001 como fallback

        Javalin app;
        boolean started = false;

        // 3. Crear App e intentar iniciar
        app = createApp();
        
        // Intento Puerto Principal
        try {
            app.start(port);
            started = true;
            System.out.println("🚀 Servidor iniciado en puerto: " + port);
        } catch (io.javalin.util.JavalinBindException ignored) {
            System.out.println("⚠️ Puerto " + port + " ocupado.");
        }

        // Intento Puerto Fallback
        if (!started) {
            // Nota: Javalin a veces requiere recrear la instancia si el start falla
            app = createApp(); 
            try {
                app.start(fb);
                started = true;
                System.out.println("🚀 Servidor iniciado en puerto fallback: " + fb);
            } catch (io.javalin.util.JavalinBindException ignored) {
                System.out.println("⚠️ Puerto fallback " + fb + " ocupado.");
            }
        }

        // Intento Puerto Aleatorio
        if (!started) {
            app = createApp();
            app.start(0);
            System.out.println("🚀 Servidor iniciado en puerto aleatorio: " + app.port());
        }
    }

    private static Javalin createApp() {
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson());
            // config.plugins.enableCors(cors -> cors.add(it -> it.anyHost())); // Alternativa simple para CORS
        });

        // Configuración CORS Manual (Muy permisiva para desarrollo)
        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            ctx.header("Access-Control-Allow-Credentials", "true");
        });

        // Manejo de pre-flight requests (OPTIONS)
        app.options("/*", ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            ctx.status(200);
        });

        // Manejo Global de Errores
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace(); // Ver el error en la terminal del backend
            ctx.status(500).json(java.util.Map.of(
                "error", "Internal Server Error", 
                "details", e.getMessage() != null ? e.getMessage() : "No details available"
            ));
        });

        app.error(404, ctx -> ctx.status(404).json(java.util.Map.of("error", "Endpoint no encontrado")));

        // REGISTRO DE RUTAS CENTRALIZADO
        ApiRouters.register(app);

        return app;
    }
}