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

    // Registrar un nuevo pedido
    public void registrarPedido(Vehiculo vehiculo, int cantidad) {
        Pedido pedido = new Pedido();
        pedido.setVehiculo(vehiculo);
        pedido.setCantidad(cantidad);
        pedido.setFecha(LocalDate.now());
        pedido.setEstado("Pendiente");
        pedidoRepositorio.save(pedido);
    }

    // Obtener todos los pedidos
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
