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

    VehiculoRepositorio vehiculoRepositorio;

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
}
