package co.ucentral.concesionario.servicios;

import co.ucentral.concesionario.persistencia.entidades.Pedido;
import co.ucentral.concesionario.persistencia.entidades.Vehiculo;
import co.ucentral.concesionario.persistencia.repositorios.PedidoRepositorio;
import co.ucentral.concesionario.persistencia.repositorios.VehiculoRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PedidoServicios {

    PedidoRepositorio pedidoRepositorio;
    VehiculoRepositorio vehiculoRepositorio;
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
        return pedidoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + id));
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

    public List<Pedido> obtenerPedidosPorEstado(String estado) {
        List<Pedido> pedidos = pedidoRepositorio.findByEstado(estado);
        if (pedidos == null) {
            return List.of(); // Retorna una lista vacía si no hay resultados
        }
        return pedidos;
    }

    public void entregarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepositorio.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("El pedido no existe."));
        Vehiculo vehiculo = pedido.getVehiculo();

        // Verificar stock disponible
        if (vehiculo.getCantidadStock() < pedido.getCantidad()) {
            throw new IllegalArgumentException("No hay suficiente stock para completar el pedido.");
        }

        // Actualizar stock
        vehiculo.setCantidadStock(vehiculo.getCantidadStock() - pedido.getCantidad());
        vehiculoRepositorio.save(vehiculo);

        // Actualizar estado del pedido
        pedido.setEstado("Exportado");
        pedidoRepositorio.save(pedido);
    }

    public List<Pedido> obtenerPedidosPendientes() {
        List<Pedido> pedidos = obtenerTodos(); // Suponiendo que existe un método obtenerTodos
        return pedidos.stream()
                .filter(pedido -> "Pendiente".equalsIgnoreCase(pedido.getEstado()))
                .collect(Collectors.toList());
    }



}
