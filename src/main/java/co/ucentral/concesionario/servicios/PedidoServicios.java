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
        Pedido pedido = pedidoRepositorio.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + pedidoId));

        Vehiculo vehiculo = pedido.getVehiculo();

        // Verificar stock disponible
        if (vehiculo.getCantidadStock() < pedido.getCantidad()) {
            throw new IllegalArgumentException("Stock insuficiente para el vehículo "
                    + vehiculo.getMarca() + " " + vehiculo.getModelo() + ". Disponible: "
                    + vehiculo.getCantidadStock() + ", solicitado: " + pedido.getCantidad());
        }

        try {
            // Actualizar stock del vehículo
            vehiculo.setCantidadStock(vehiculo.getCantidadStock() - pedido.getCantidad());
            vehiculoRepositorio.save(vehiculo);

            // Actualizar inventario
            Inventario inventario = inventarioRepositorio.findByVehiculoId(vehiculo.getId());
            if (inventario == null) {
                inventario = new Inventario();
                inventario.setVehiculo(vehiculo);
                inventario.setCantidad(0);
            }

            inventario.setCantidad(inventario.getCantidad() + pedido.getCantidad());
            inventarioRepositorio.save(inventario);

            // Cambiar el estado del pedido
            pedido.setEstado("Entregado");
            pedidoRepositorio.save(pedido);

            // Eliminar pedido entregado

        } catch (Exception e) {
            // Lanzar una excepción para que el controlador la maneje
            throw new RuntimeException("Error al procesar el pedido: " + e.getMessage());
        }
    }



    /**
     * Obtener todos los pedidos pendientes.
     */
    public List<Pedido> obtenerPedidosPendientes() {
        return pedidoRepositorio.findByEstado("Pendiente");
    }
}
