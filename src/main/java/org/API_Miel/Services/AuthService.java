package org.API_Miel.Services;

import org.API_Miel.Models.Usuario;
import org.API_Miel.Repositories.UsuarioRepository;
import com.password4j.Hash;
import com.password4j.Password;

public class AuthService {
    private final UsuarioRepository repo = new UsuarioRepository();

    public Usuario login(String email, String plain) {
        if (email == null || plain == null) return null;
        String e = email.trim().toLowerCase();
        Usuario u = repo.findByEmail(e);
        if (u == null) return null;
        String stored = u.getContrasenia();
        if (stored == null || stored.isEmpty()) return null;
        boolean ok = Password.check(plain, stored).withBcrypt();
        if (!ok) return null;
        return u;
    }

   public long register(String nombre, String email, String plain, String celular, int idRol) {
    if (email == null || plain == null) return 0;
    String e = email.trim().toLowerCase();
    if (repo.findByEmail(e) != null) return -1;
    Hash h = Password.hash(plain).withBcrypt();
    Usuario u = new Usuario();
    u.setNombreCompleto(nombre == null ? "" : nombre.trim());
    u.setCorreo(e);
    u.setContrasenia(h.getResult());
    u.setNumCelular(celular == null ? "" : celular.trim());
    u.setIdRol(idRol); 
    return repo.create(u);
}

    public boolean emailDisponible(String email) {
        if (email == null) return false;
        String e = email.trim().toLowerCase();
        return repo.findByEmail(e) == null;
    }

    
}