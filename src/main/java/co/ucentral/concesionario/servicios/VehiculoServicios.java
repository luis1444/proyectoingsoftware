package co.ucentral.concesionario.servicios;

import co.ucentral.concesionario.persistencia.entidades.Vehiculo;
import co.ucentral.concesionario.persistencia.repositorios.VehiculoRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class VehiculoServicios {

    private final VehiculoRepositorio vehiculoRepositorio;

    public void registrarVehiculo(Vehiculo vehiculo) {
        vehiculoRepositorio.save(vehiculo);
    }

    public List<Vehiculo> obtenerTodos() {
        return (List<Vehiculo>) vehiculoRepositorio.findAll();
    }

    public boolean borrar(Vehiculo vehiculo) {
        try {
            vehiculoRepositorio.delete(vehiculo);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Vehiculo obtenerPorId(Long id) {
        Optional<Vehiculo> vehiculo = vehiculoRepositorio.findById(id);
        return vehiculo.orElse(null);
    }

    // Método para actualizar un vehículo
    public void actualizarVehiculo(Vehiculo vehiculo) {
        Optional<Vehiculo> vehiculoExistente = vehiculoRepositorio.findById(vehiculo.getId());
        if (vehiculoExistente.isPresent()) {
            Vehiculo vehiculoActualizado = vehiculoExistente.get();
            vehiculoActualizado.setMarca(vehiculo.getMarca());
            vehiculoActualizado.setModelo(vehiculo.getModelo());
            vehiculoActualizado.setAnio(vehiculo.getAnio());
            vehiculoActualizado.setColor(vehiculo.getColor());
            vehiculoActualizado.setPrecio(vehiculo.getPrecio());
            vehiculoActualizado.setTipoCombustible(vehiculo.getTipoCombustible());
            vehiculoActualizado.setPaisOrigen(vehiculo.getPaisOrigen());
            vehiculoActualizado.setFoto(vehiculo.getFoto());
            vehiculoRepositorio.save(vehiculoActualizado);
        } else {
            throw new RuntimeException("Vehículo no encontrado con ID: " + vehiculo.getId());
        }
    }

    // Método para fabricar vehículos
    public void fabricarVehiculos(Long idVehiculo, int cantidad) {
        System.out.println("Buscando vehículo con ID: " + idVehiculo);

        Vehiculo vehiculo = obtenerPorId(idVehiculo);
        if (vehiculo == null) {
            System.out.println("Vehículo no encontrado.");
            throw new IllegalArgumentException("Vehículo con ID " + idVehiculo + " no encontrado.");
        }

        System.out.println("Vehículo encontrado: " + vehiculo);

        if (cantidad <= 0) {
            System.out.println("Cantidad no válida: " + cantidad);
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }

        vehiculo.setCantidadStock(vehiculo.getCantidadStock() + cantidad);
        System.out.println("Nueva cantidad en stock: " + vehiculo.getCantidadStock());

        vehiculoRepositorio.save(vehiculo);
        System.out.println("Vehículo actualizado correctamente.");
    }

    // Método para obtener todos los vehículos
    public List<Vehiculo> obtenerTodosLosVehiculos() {
        return vehiculoRepositorio.findAll();
    }
    public Vehiculo obtenerVehiculoPorNombre(String modelo) {
        return vehiculoRepositorio.findByModelo(modelo); // Asumiendo que tienes un método en el repositorio para esto
    }

}
