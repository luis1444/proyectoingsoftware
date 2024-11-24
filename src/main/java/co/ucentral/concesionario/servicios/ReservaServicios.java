package co.ucentral.concesionario.servicios;

import co.ucentral.concesionario.persistencia.entidades.Reserva;
import co.ucentral.concesionario.persistencia.repositorios.ReservaRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ReservaServicios {

    private final ReservaRepositorio reservaRepositorio;

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
}
