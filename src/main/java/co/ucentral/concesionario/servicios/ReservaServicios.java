package co.ucentral.concesionario.servicios;

import co.ucentral.concesionario.persistencia.entidades.Reserva;
import co.ucentral.concesionario.persistencia.repositorios.ReservaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaServicios {

    @Autowired
    private ReservaRepositorio reservaRepositorio;

    // Obtener todas las reservas
    public List<Reserva> obtenerTodasLasReservas() {
        return reservaRepositorio.findAll();
    }

    // Obtener una reserva por ID
    public Reserva obtenerReservaPorId(Long id) {
        return reservaRepositorio.findById(id).orElse(null);
    }

    // Guardar una nueva reserva
    public Reserva guardarReserva(Reserva reserva) {
        return reservaRepositorio.save(reserva);
    }

    public List<Reserva> obtenerReservasPendientes() {
        return reservaRepositorio.findByEstado("Pendiente"); // Filtrar por estado 'Pendiente'
    }

}
