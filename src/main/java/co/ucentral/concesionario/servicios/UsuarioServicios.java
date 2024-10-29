package co.ucentral.concesionario.servicios;
import co.ucentral.concesionario.persistencia.entidades.Usuario;
import co.ucentral.concesionario.persistencia.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class UsuarioServicios {
    UsuarioRepositorio usuarioRepositorio;


    @Autowired
    UsuarioRepositorio UsuarioRepositorio;
    public void registrarUsuario(Usuario usuario) {
        usuarioRepositorio.save(usuario);
    }

    public List<Usuario> obtenerTodos(){
        List<Usuario> listado = (List<Usuario>) usuarioRepositorio.findAll();
        return listado;
    }
    public boolean borrar(Usuario usuario){
        try{
            usuarioRepositorio.delete(usuario);
        }catch (Exception e){
            return false;
        }
        return true;
    }

}
