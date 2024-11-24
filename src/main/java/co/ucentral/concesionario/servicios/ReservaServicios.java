package co.ucentral.concesionario.servicios;

import co.ucentral.concesionario.persistencia.entidades.Inventario;
import co.ucentral.concesionario.persistencia.entidades.Reserva;
import co.ucentral.concesionario.persistencia.repositorios.ReservaRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ReservaServicios {

    private final ReservaRepositorio reservaRepositorio;
    InventarioServicios inventarioServicios;

    /**
     * Obtener todas las reservas.
     */
    public List<Reserva> obtenerTodasLasReservas() {
        return reservaRepositorio.findAll();
    }

    /**
     * Obtener una reserva por su ID.
     */
    public Reserva obtenerReservaPorId(Long id) {
        Optional<Reserva> reserva = reservaRepositorio.findById(id);
        return reserva.orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + id));
    }

    /**
     * Obtener todas las reservas pendientes.
     */
    public List<Reserva> obtenerReservasPendientes() {
        return reservaRepositorio.findByEstado("Pendiente");
    }

    /**
     * Guardar o actualizar una reserva.
     */
    public Reserva guardarReserva(Reserva reserva) {
        return reservaRepositorio.save(reserva); // Devuelve la entidad guardada
    }

    /**
     * Marcar una reserva como entregada.
     */
    public void marcarReservaComoEntregada(Long id) {
        Reserva reserva = obtenerReservaPorId(id);
        reserva.setEstado("Entregado");
        reservaRepositorio.save(reserva);
    }

    /**
     * Cancelar una reserva.
     */
    public void cancelarReserva(Long id) {
        Reserva reserva = obtenerReservaPorId(id);
        reserva.setEstado("Cancelada");
        reservaRepositorio.save(reserva);
    }
    @Transactional
    public void entregarReserva(Long reservaId) {
        // Obtener la reserva
        Reserva reserva = obtenerReservaPorId(reservaId);

        if (!"Pendiente".equals(reserva.getEstado())) {
            throw new IllegalStateException("Solo se pueden entregar reservas en estado 'Pendiente'.");
        }

        // Obtener el inventario del vehículo asociado
        Inventario inventario = inventarioServicios.obtenerPorVehiculoId(reserva.getVehiculo().getId());
        if (inventario == null) {
            throw new IllegalStateException("No se encontró inventario para el vehículo asociado.");
        }

        // Verificar que haya suficiente cantidad en el inventario
        if (inventario.getCantidad() <= 0) {
            throw new IllegalStateException("No hay suficiente inventario para entregar esta reserva.");
        }

        // Reducir la cantidad del inventario
        inventario.setCantidad(inventario.getCantidad() - 1);
        inventarioServicios.actualizarCantidadInventario(inventario.getId(), -1);

        // Cambiar el estado de la reserva a 'Entregado'
        reserva.setEstado("Entregado");
        guardarReserva(reserva);
    }
}
