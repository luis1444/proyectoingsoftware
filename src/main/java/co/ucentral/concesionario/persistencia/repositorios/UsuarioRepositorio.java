package co.ucentral.concesionario.persistencia.repositorios;

import co.ucentral.concesionario.persistencia.entidades.Usuario;
import org.springframework.data.repository.CrudRepository;
public interface UsuarioRepositorio extends CrudRepository<Usuario, String> {
}
