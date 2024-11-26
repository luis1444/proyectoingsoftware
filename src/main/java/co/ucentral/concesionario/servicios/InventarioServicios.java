package co.ucentral.concesionario.servicios;

import co.ucentral.concesionario.persistencia.entidades.Inventario;
import co.ucentral.concesionario.persistencia.entidades.Vehiculo;
import co.ucentral.concesionario.persistencia.repositorios.InventarioRepositorio;
import co.ucentral.concesionario.persistencia.repositorios.VehiculoRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class InventarioServicios {

    private final InventarioRepositorio inventarioRepositorio;
    private final VehiculoRepositorio vehiculoRepositorio;

    // Registrar un nuevo inventario
    public void registrarInventario(Inventario inventario) {
        inventarioRepositorio.save(inventario);
    }

    // Obtener todos los inventarios
    public List<Inventario> obtenerTodos() {
        return inventarioRepositorio.findAll();
    }

    // Obtener un inventario por ID
    public Inventario obtenerPorId(Long id) {
        Optional<Inventario> inventario = inventarioRepositorio.findById(id);
        return inventario.orElse(null);
    }

    // Obtener un inventario por ID de vehículo
    public Inventario obtenerPorVehiculoId(Long vehiculoId) {
        return inventarioRepositorio.findByVehiculoId(vehiculoId);
    }

    // Método para actualizar la cantidad en el inventario de un vehículo
    public void actualizarCantidadInventario(Long id, int cantidadAdicional) {
        Optional<Inventario> inventarioOpt = inventarioRepositorio.findById(id);

        if (inventarioOpt.isPresent()) {
            Inventario inventarioExistente = inventarioOpt.get();
            inventarioExistente.setCantidad(inventarioExistente.getCantidad() + cantidadAdicional);
            inventarioRepositorio.save(inventarioExistente);
        } else {
            throw new RuntimeException("Inventario no encontrado para el ID: " + id);
        }
    }

    // Método para restar vehículos del inventario al realizar una venta
    public void venderVehiculo(Long idVehiculo, int cantidad) {
        Optional<Inventario> inventario = inventarioRepositorio.findAll().stream()
                .filter(inv -> inv.getVehiculo().getId().equals(idVehiculo))
                .findFirst();

        if (inventario.isPresent()) {
            Inventario inventarioExistente = inventario.get();

            if (inventarioExistente.getCantidad() >= cantidad) {
                inventarioExistente.setCantidad(inventarioExistente.getCantidad() - cantidad);
                inventarioRepositorio.save(inventarioExistente);
                System.out.println("Venta realizada correctamente. Nueva cantidad en inventario: " + inventarioExistente.getCantidad());
            } else {
                throw new RuntimeException("No hay suficiente cantidad en el inventario.");
            }
        } else {
            throw new RuntimeException("Inventario no encontrado para el vehículo con ID: " + idVehiculo);
        }
    }

    // Método para obtener todos los vehículos en el inventario
    public List<Inventario> obtenerInventarioDeVehiculos() {
        return inventarioRepositorio.findAll();
    }
}