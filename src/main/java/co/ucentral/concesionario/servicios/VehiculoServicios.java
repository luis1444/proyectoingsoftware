package co.ucentral.concesionario.servicios;

import co.ucentral.concesionario.persistencia.entidades.Vehiculo;
import co.ucentral.concesionario.persistencia.repositorios.VehiculoRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
