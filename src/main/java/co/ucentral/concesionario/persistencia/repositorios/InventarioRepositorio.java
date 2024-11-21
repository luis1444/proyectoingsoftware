package co.ucentral.concesionario.persistencia.repositorios;

import co.ucentral.concesionario.persistencia.entidades.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventarioRepositorio extends JpaRepository<Inventario, Long> {
    public Inventario findByVehiculoId(Long vehiculoId);
}