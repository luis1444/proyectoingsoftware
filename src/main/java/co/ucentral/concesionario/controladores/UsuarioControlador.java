package co.ucentral.concesionario.controladores;
import co.ucentral.concesionario.persistencia.entidades.Usuario;
import co.ucentral.concesionario.servicios.UsuarioServicios;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;
@AllArgsConstructor
@Controller
public class UsuarioControlador {

    UsuarioServicios usuarioServicio;

    public List<Usuario> obtenerTodos() {
        return usuarioServicio.obtenerTodos();
    }
}
