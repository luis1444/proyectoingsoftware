package co.ucentral.concesionario.persistencia.repositorios;

import co.ucentral.concesionario.persistencia.entidades.Cliente;
import co.ucentral.concesionario.persistencia.entidades.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCorreoElectronico(String correoElectronico);

}
