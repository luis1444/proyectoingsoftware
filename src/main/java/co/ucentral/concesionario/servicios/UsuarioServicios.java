package co.ucentral.concesionario.servicios;

import co.ucentral.concesionario.persistencia.entidades.Usuario;
import co.ucentral.concesionario.persistencia.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioServicios {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public void registrarUsuario(Usuario usuario) {
        usuarioRepositorio.save(usuario);
    }

    public Usuario autenticarUsuario(String nombreUsuario, String contrasena) {
        Usuario usuarioEncontrado = usuarioRepositorio.findById(nombreUsuario).orElse(null);
        if (usuarioEncontrado != null) {
            System.out.println("Usuario encontrado: " + usuarioEncontrado.getUsuario());
            // Ignorar temporalmente la verificación de la contraseña
            return usuarioEncontrado;
        } else {
            System.out.println("Usuario no encontrado");
        }
        return null;
    }

    public List<Usuario> obtenerTodos() {
        return (List<Usuario>) usuarioRepositorio.findAll();
    }

    public boolean borrar(Usuario usuario) {
        try {
            usuarioRepositorio.delete(usuario);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
