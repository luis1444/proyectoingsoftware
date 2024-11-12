package co.ucentral.concesionario.servicios;

import co.ucentral.concesionario.persistencia.entidades.Pedido;
import co.ucentral.concesionario.persistencia.entidades.Vehiculo;
import co.ucentral.concesionario.persistencia.repositorios.PedidoRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PedidoServicios {

    PedidoRepositorio pedidoRepositorio;
    private final VehiculoServicios vehiculoServicios;



    public void registrarPedido(Long vehiculoId, int cantidad, String pais) {
        // Verificar que el vehículo existe
        Vehiculo vehiculo = vehiculoServicios.obtenerPorId(vehiculoId);
        if (vehiculo == null) {
            throw new RuntimeException("Vehículo no encontrado con ID: " + vehiculoId);
        }

        // Crear un nuevo pedido
        Pedido pedido = new Pedido();
        pedido.setVehiculo(vehiculo);
        pedido.setCantidad(cantidad);
        pedido.setPais(pais);
        pedido.setFecha(LocalDate.now()); // Fecha actual
        pedido.setEstado("Pendiente"); // Estado inicial

        // Guardar el pedido
        pedidoRepositorio.save(pedido);
    }


    public List<Pedido> obtenerTodos() {
        return pedidoRepositorio.findAll();
    }

    // Obtener pedido por ID
    public Pedido obtenerPorId(Long id) {
        Optional<Pedido> pedido = pedidoRepositorio.findById(id);
        return pedido.orElse(null);
    }

    // Actualizar estado de un pedido
    public void actualizarEstado(Long id, String nuevoEstado) {
        Optional<Pedido> pedidoExistente = pedidoRepositorio.findById(id);
        if (pedidoExistente.isPresent()) {
            Pedido pedido = pedidoExistente.get();
            pedido.setEstado(nuevoEstado);
            pedidoRepositorio.save(pedido);
        } else {
            throw new RuntimeException("Pedido no encontrado con ID: " + id);
        }
    }


    public boolean borrarPedido(Long id) {
        try {
            pedidoRepositorio.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
