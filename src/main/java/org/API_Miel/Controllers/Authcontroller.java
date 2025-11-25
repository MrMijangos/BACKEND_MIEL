package org.API_Miel.Controllers;

import io.javalin.http.Context;
import org.API_Miel.Services.AuthService;
import org.API_Miel.Models.Usuario;

import java.util.Map;

public class Authcontroller {
    private final AuthService service = new AuthService();

    public void register(Context ctx) {
        Map<String, Object> body = ctx.bodyAsClass(Map.class);
        
        // DEBUG
        System.out.println("=== REGISTER ATTEMPT ===");
        System.out.println("BODY completo: " + body);
        System.out.println("Nombre: " + body.get("nombre"));
        System.out.println("Correo: " + body.get("correo"));
        System.out.println("Contrasenia: " + body.get("contrasenia"));
        System.out.println("Celular: " + body.get("celular"));
        
         String nombre = (String) body.get("nombre");
         String correo = (String) body.get("correo");
        String contrasenia = (String) body.get("contrasenia");
        String celular = (String) body.get("celular");
        Integer idRol = body.get("rol") != null ? ((Number) body.get("rol")).intValue() : 2; // 1=ADMIN, 2=USER

         long id = service.register(nombre, correo, contrasenia, celular, idRol);
   
        
        if (id == -1) { 
            System.out.println(" REGISTER FALLIDO - Correo ya existe");
            ctx.status(409).json(Map.of("error", "Correo ya registrado")); 
            return; 
        }
        
        System.out.println(" REGISTER EXITOSO - ID: " + id);
        ctx.json(Map.of("idUsuario", id));
    }

    public void login(Context ctx) {
        Map<String, Object> body = ctx.bodyAsClass(Map.class);
        
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("BODY completo: " + body);
        System.out.println("Correo: " + body.get("correo"));
        System.out.println("Contrasenia: " + body.get("contrasenia"));
        
        String correo = (String) body.get("correo");
        String contrasenia = (String) body.get("contrasenia");
        Usuario u = service.login(correo, contrasenia);
        
        if (u == null) { 
            System.out.println(" LOGIN FALLIDO - Credenciales inválidas");
            ctx.status(401).json(Map.of("error", "Credenciales invalidas")); 
            return; 
        }
        
        System.out.println("LOGIN EXITOSO - Usuario: " + u.getNombreCompleto());
        String rol = u.getIdRol() == 1 ? "ADMIN" : "USER";
        ctx.json(Map.of(
                "idUsuario", u.getIdUsuario(),
                "nombre", u.getNombreCompleto(),
                "correo", u.getCorreo(),
                "rol", rol
        ));
    }
}