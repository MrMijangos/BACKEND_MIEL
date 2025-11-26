package org.API_Miel.Usuario.services;

import org.API_Miel.Usuario.models.LoginRequest;
import org.API_Miel.Usuario.models.RegisterRequest;
import org.API_Miel.Usuario.models.UpdateUsuarioRequest;
import org.API_Miel.Usuario.models.Usuario;
import org.API_Miel.Usuario.models.UsuarioResponse;
import org.API_Miel.Usuario.repositories.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    
    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepository();
    }
    
    // Registrar nuevo usuario
    public UsuarioResponse register(RegisterRequest request) {
        // Validar que el correo no exista
        if (usuarioRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }
        
        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(request.getNombreCompleto());
        usuario.setCorreo(request.getCorreo());
        usuario.setContrasena(hashPassword(request.getContrasena())); // Hash de la contraseña
        usuario.setNumCelular(request.getNumCelular());
        usuario.setRolId(2); // Rol de usuario por defecto
        usuario.setActivo(true);
        
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return new UsuarioResponse(savedUsuario); // Sin contraseña
    }
    
    // Login
    public UsuarioResponse login(LoginRequest request) {
        // Buscar usuario por correo
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
            .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
        
        // Verificar si está activo
        if (!usuario.getActivo()) {
            throw new RuntimeException("Usuario desactivado");
        }
        
        // Verificar contraseña
        if (!checkPassword(request.getContrasena(), usuario.getContrasena())) {
            throw new RuntimeException("Credenciales inválidas");
        }
        
        // Actualizar última conexión
        usuarioRepository.updateUltimaConexion(usuario.getId());
        
        return new UsuarioResponse(usuario);
    }
    
    // Obtener usuario por ID
    public UsuarioResponse getById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return new UsuarioResponse(usuario);
    }
    
    // Actualizar usuario
    public UsuarioResponse update(Long id, UpdateUsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (request.getNombreCompleto() != null) {
            usuario.setNombreCompleto(request.getNombreCompleto());
        }
        if (request.getNumCelular() != null) {
            usuario.setNumCelular(request.getNumCelular());
        }
        
        Usuario updatedUsuario = usuarioRepository.update(usuario);
        return new UsuarioResponse(updatedUsuario);
    }
    
    // Listar todos los usuarios
    public List<UsuarioResponse> getAll() {
        return usuarioRepository.findAll().stream()
            .map(UsuarioResponse::new)
            .collect(Collectors.toList());
    }
    
    // Desactivar usuario (soft delete)
    public void delete(Long id) {
        if (!usuarioRepository.findById(id).isPresent()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.delete(id);
    }
    
    // ===== MÉTODOS PRIVADOS PARA CONTRASEÑAS =====
    
    // Hashear contraseña con BCrypt
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
    
    // Verificar contraseña
    private boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}