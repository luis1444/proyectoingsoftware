package co.ucentral.concesionario.persistencia.repositorios;


import co.ucentral.concesionario.persistencia.entidades.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepositorio extends JpaRepository<Reserva, Long> {
    List<Reserva> findByEstado(String estado);

}
