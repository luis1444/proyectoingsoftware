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
    public String consultarEstadoReserva(@PathVariable Long id, Model model) {
        Reserva reserva = reservaServicios.obtenerReservaPorId(id);
        if (reserva == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada");
        }
        model.addAttribute("reserva", reserva);
        return "estadoReserva"; // Renderiza la vista estadoReserva.html
    }

    /**
     * Marcar una reserva como entregada.
     */
    @PostMapping("/marcarEntregada/{id}")
    public String marcarReservaEntregada(@PathVariable Long id, Model model) {
        Reserva reserva = reservaServicios.obtenerReservaPorId(id);
        if (reserva != null) {
            reserva.setEstado("Entregado");
            reservaServicios.guardarReserva(reserva);
            model.addAttribute("reserva", reserva);
            model.addAttribute("mensaje", "La reserva ha sido marcada como entregada.");
        } else {
            model.addAttribute("error", "No se encontró la reserva.");
        }
        return "estadoReserva"; // Renderiza la vista con el estado actualizado
    }

    /**
     * Cancelar una reserva.
     */
    @PostMapping("/cancelar/{id}")
    public String cancelarReserva(@PathVariable Long id, Model model) {
        Reserva reserva = reservaServicios.obtenerReservaPorId(id);
        if (reserva != null) {
            reserva.setEstado("Cancelada");
            reservaServicios.guardarReserva(reserva);
            model.addAttribute("reserva", reserva);
            model.addAttribute("mensaje", "La reserva ha sido cancelada.");
        } else {
            model.addAttribute("error", "No se encontró la reserva.");
        }
        return "estadoReserva"; // Renderiza la vista con el estado actualizado
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
        List<Reserva> reservasPendientes = reservaServicios.obtenerReservasPendientes();

        if (reservasPendientes.isEmpty()) {
            model.addAttribute("mensaje", "No hay reservas pendientes para entregar.");
        } else {
            model.addAttribute("reservas", reservasPendientes);
        }

        return "entregarReservas"; // Renderiza la vista Thymeleaf
    }
    @PostMapping("/entregar")
    public String entregarReserva(@RequestParam("reservaId") Long reservaId, Model model) {
        try {
            reservaServicios.entregarReserva(reservaId);
            model.addAttribute("mensaje", "Reserva marcada como entregada exitosamente.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", "Error al entregar la reserva: " + e.getMessage());
        }

        return "redirect:/reservas/pendientes"; // Redirige para actualizar la vista
    }

}
