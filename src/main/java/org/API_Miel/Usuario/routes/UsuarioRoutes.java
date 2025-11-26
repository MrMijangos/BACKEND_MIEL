package org.API_Miel.Usuario.routes;

import org.API_Miel.Usuario.controllers.UsuarioController;
import io.javalin.Javalin;

public class UsuarioRoutes {
    private final UsuarioController usuarioController;
    
    public UsuarioRoutes() {
        this.usuarioController = new UsuarioController();
    }
    
    public void register(Javalin app) {
        // Rutas de autenticación
        app.post("/api/auth/register", usuarioController::register);
        app.post("/api/auth/login", usuarioController::login);
        
        // Rutas de usuario
        app.get("/api/usuarios", usuarioController::getAll);
        app.get("/api/usuarios/{id}", usuarioController::getById);
        app.put("/api/usuarios/{id}", usuarioController::update);
        app.delete("/api/usuarios/{id}", usuarioController::delete);
    }
}