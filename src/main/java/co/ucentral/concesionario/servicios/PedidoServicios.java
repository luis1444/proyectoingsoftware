package co.ucentral.concesionario.servicios;

import co.ucentral.concesionario.persistencia.entidades.Inventario;
import co.ucentral.concesionario.persistencia.entidades.Pedido;
import co.ucentral.concesionario.persistencia.entidades.Vehiculo;
import co.ucentral.concesionario.persistencia.repositorios.InventarioRepositorio;
import co.ucentral.concesionario.persistencia.repositorios.PedidoRepositorio;
import co.ucentral.concesionario.persistencia.repositorios.VehiculoRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PedidoServicios {

    private final PedidoRepositorio pedidoRepositorio;
    private final VehiculoRepositorio vehiculoRepositorio;
    private final InventarioRepositorio inventarioRepositorio;
    private final VehiculoServicios vehiculoServicios;

    /**
     * Registrar un nuevo pedido.
     */
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
        pedido.setFecha(LocalDate.now());
        pedido.setEstado("Pendiente"); // Estado inicial

        // Guardar el pedido
        pedidoRepositorio.save(pedido);
    }

    /**
     * Obtener todos los pedidos.
     */
    public List<Pedido> obtenerTodos() {
        return pedidoRepositorio.findAll();
    }

    /**
     * Obtener un pedido por ID.
     */
    public Pedido obtenerPorId(Long id) {
        return pedidoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + id));
    }

    /**
     * Actualizar el estado de un pedido.
     */
    public void actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = obtenerPorId(id);
        pedido.setEstado(nuevoEstado);
        pedidoRepositorio.save(pedido);
    }

    /**
     * Borrar un pedido por ID.
     */
    public boolean borrarPedido(Long id) {
        try {
            pedidoRepositorio.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtener pedidos por estado.
     */
    public List<Pedido> obtenerPedidosPorEstado(String estado) {
        List<Pedido> pedidos = pedidoRepositorio.findByEstado(estado);
        return pedidos != null ? pedidos : List.of();
    }

    /**
     * Entregar un pedido.
     */
    @Transactional
    public void entregarPedido(Long pedidoId) {
        // Buscar el pedido por su ID
        Pedido pedido = pedidoRepositorio.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("El pedido no existe."));

        Vehiculo vehiculo = pedido.getVehiculo();

        // Buscar o crear el inventario relacionado con el vehículo
        Inventario inventario = inventarioRepositorio.findByVehiculoId(vehiculo.getId());
        if (inventario == null) {
            // Si no existe inventario, crearlo
            inventario = new Inventario();
            inventario.setVehiculo(vehiculo);
            inventario.setCantidad(0); // Inicializar la cantidad en 0
        }

        // Actualizar la cantidad del inventario
        inventario.setCantidad(inventario.getCantidad() + pedido.getCantidad());
        inventarioRepositorio.save(inventario);

        // Actualizar el estado del pedido a "Entregado"
        pedido.setEstado("Entregado");
        pedidoRepositorio.save(pedido);

        // Eliminar el pedido de la base de datos
        pedidoRepositorio.delete(pedido);
    }

    /**
     * Obtener todos los pedidos pendientes.
     */
    public List<Pedido> obtenerPedidosPendientes() {
        return pedidoRepositorio.findByEstado("Pendiente");
    }
}
