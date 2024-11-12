package co.ucentral.concesionario.controladores;

import co.ucentral.concesionario.persistencia.entidades.Usuario;
import co.ucentral.concesionario.servicios.UsuarioServicios;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@AllArgsConstructor
@Controller
public class UsuarioControlador {

     UsuarioServicios usuarioServicio;

    @GetMapping("/registro")
    public String mostrarFormularioDeRegistro(Model model) {
        Usuario usuario = new Usuario();
        model.addAttribute("elusuario", usuario);
        return "registro"; // Vista de registro.html
    }

    @PostMapping("/registro")
    public String registrarUsuario(Usuario usuario, Model model) {
        usuarioServicio.registrarUsuario(usuario);
        model.addAttribute("mensaje", "Usuario registrado exitosamente");
        return "iniciosesion"; // Redirigir a iniciosesion.html después de registrar
    }

    @GetMapping("/iniciosesion")
    public String mostrarInicioSesion(Model model) {

        return "iniciosesion";
    }

    @PostMapping("/iniciar-sesion")
    public String iniciarSesion(String usuario, String contrasena, Model model) {
        Usuario usuarioAutenticado = usuarioServicio.autenticarUsuario(usuario, contrasena);

        if (usuarioAutenticado != null) {
            switch (usuarioAutenticado.getRol()) {
                case "cliente":
                    return "redirect:/pantallaCliente";
                case "fabricante":
                    return "redirect:/pantallaFabricante";
                case "administrador":
                    return "redirect:/pantallaAdministrador";
                default:
                    model.addAttribute("error", "Rol no reconocido");
                    return "iniciosesion";
            }
        } else {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "iniciosesion";
        }
    }

    @GetMapping("/pantallaFabricante")
    public String mostrarPantallaFabricante(Model model) {
        return "pantallaFabricante";
    }

    @GetMapping("/pantallaAdministrador")
    public String mostrarPantallaAdministrador(Model model) {
        return "pantallaAdministrador";
    }


}
