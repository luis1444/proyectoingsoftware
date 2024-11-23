package co.ucentral.concesionario.controladores;

import co.ucentral.concesionario.persistencia.entidades.Reserva;
import co.ucentral.concesionario.servicios.ReservaServicios;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/reservas")
public class ReservasControlador {

    @Autowired
    private ReservaServicios reservaServicios;

    /**
     * Mostrar todas las reservas.
     */
    @GetMapping("/verReservas")
    public String verReservas(Model model) {
        List<Reserva> reservas = reservaServicios.obtenerTodasLasReservas();
        model.addAttribute("reservas", reservas);
        return "verReservas"; // Renderiza la vista "verReservas.html"
    }

    /**
     * Consultar estado de una reserva y devolver en formato JSON.
     */
    @GetMapping("/reservaConfirmada/{id}")
    @ResponseBody
    public Reserva consultarEstadoReserva(@PathVariable Long id) {
        Reserva reserva = reservaServicios.obtenerReservaPorId(id);
        if (reserva == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada");
        }
        return reserva; // Retorna la reserva como JSON
    }

    /**
     * Marcar una reserva como entregada.
     */
    @PostMapping("/marcarEntregada/{id}")
    public String marcarReservaEntregada(@PathVariable Long id, Model model) {
        try {
            Reserva reserva = reservaServicios.obtenerReservaPorId(id);
            if (reserva != null) {
                reserva.setEstado("Entregado");
                reservaServicios.guardarReserva(reserva);
                model.addAttribute("mensaje", "Reserva marcada como entregada");
                model.addAttribute("reserva", reserva);
                return "estadoReserva"; // Renderiza la vista "estadoReserva.html"
            } else {
                model.addAttribute("error", "Reserva no encontrada");
                return "error"; // Renderiza la vista "error.html"
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al marcar la reserva como entregada: " + e.getMessage());
            return "error"; // Renderiza la vista "error.html"
        }
    }

    /**
     * Cancelar una reserva.
     */
    @PostMapping("/cancelar/{id}")
    public String cancelarReserva(@PathVariable Long id, Model model) {
        try {
            Reserva reserva = reservaServicios.obtenerReservaPorId(id);
            if (reserva != null) {
                reserva.setEstado("Cancelada");
                reservaServicios.guardarReserva(reserva);
                model.addAttribute("mensaje", "Reserva cancelada con éxito");
                model.addAttribute("reserva", reserva);
                return "estadoReserva"; // Renderiza la vista "estadoReserva.html"
            } else {
                model.addAttribute("error", "Reserva no encontrada");
                return "error"; // Renderiza la vista "error.html"
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al cancelar la reserva: " + e.getMessage());
            return "error"; // Renderiza la vista "error.html"
        }
    }

    /**
     * Consultar todas las reservas desde el panel administrativo.
     */
    @GetMapping("/consultarReservas")
    public String consultarReservasAdmin(Model model) {
        List<Reserva> reservas = reservaServicios.obtenerTodasLasReservas();
        model.addAttribute("reservas", reservas);
        return "consultarReservas"; // Renderiza la vista "consultarReservas.html"
    }

    /**
     * Mostrar las reservas pendientes.
     */
    @GetMapping("/pendientes")
    @Transactional
    public String mostrarReservasPendientes(Model model) {
        try {
            List<Reserva> reservasPendientes = reservaServicios.obtenerReservasPendientes();

            // Verificar si hay reservas pendientes
            if (reservasPendientes.isEmpty()) {
                model.addAttribute("mensaje", "No hay reservas pendientes para entregar.");
            } else {
                model.addAttribute("reservas", reservasPendientes);
            }

            return "entregarReservas"; // Retorna la vista "entregarReservas.html"
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar las reservas pendientes: " + e.getMessage());
            return "error"; // Redirige a la vista de error si ocurre algún problema
        }
    }
}
