
package org.API_Miel;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.json.JavalinJackson;
import io.github.cdimascio.dotenv.Dotenv;
import org.API_Miel.ConfigDB.ConfigDB;
import org.API_Miel.Routers.ApiRouters;

public class Main {
    public static void main(String[] args) {
        try {
            ConfigDB.getDataSource();
        } catch (RuntimeException ignored) {
        }
        Dotenv dotenv = Dotenv.configure().directory("./").ignoreIfMissing().load();
        String portStr = dotenv.get("PORT");
        int port = portStr != null && !portStr.isEmpty() ? Integer.parseInt(portStr) : 8081;
        String fbStr = dotenv.get("PORT_FALLBACK");
        int fb = fbStr != null && !fbStr.isEmpty() ? Integer.parseInt(fbStr) : 7000;

        Javalin app;
        boolean started = false;
        app = createApp();
        try {
            app.start(port);
            started = true;
        } catch (io.javalin.util.JavalinBindException ignored) {
        }
        if (!started) {
            app = createApp();
            try {
                app.start(fb);
                started = true;
            } catch (io.javalin.util.JavalinBindException ignored) {
            }
        }
        if (!started) {
            app = createApp();
            app.start(0);
        }
    }

    private static Javalin createApp() {
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson());
        });
        app.before(ctx -> ctx.header("Access-Control-Allow-Origin", "*"));
        app.before(ctx -> ctx.header("Access-Control-Allow-Headers", "Authorization, Content-Type"));
        app.before(ctx -> ctx.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS"));
        app.before(ctx -> ctx.header("Access-Control-Allow-Credentials", "true"));
        app.options("/*", Context::status);
        app.exception(Exception.class, (e, ctx) -> ctx.status(500).json(java.util.Map.of("error", e.getMessage())));
        app.error(404, ctx -> ctx.json(java.util.Map.of("error", "Not Found")));
        ApiRouters.register(app);
        return app;
    }

}
