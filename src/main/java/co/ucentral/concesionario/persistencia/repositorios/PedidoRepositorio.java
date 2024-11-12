package co.ucentral.concesionario.persistencia.repositorios;

import co.ucentral.concesionario.persistencia.entidades.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {

}