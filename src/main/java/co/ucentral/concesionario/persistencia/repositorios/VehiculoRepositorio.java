package co.ucentral.concesionario.persistencia.repositorios;

import co.ucentral.concesionario.persistencia.entidades.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface VehiculoRepositorio extends JpaRepository<Vehiculo, Long> {
}
