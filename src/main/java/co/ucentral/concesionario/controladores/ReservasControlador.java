package co.ucentral.concesionario.controladores;

import co.ucentral.concesionario.persistencia.entidades.Reserva;
import co.ucentral.concesionario.servicios.ReservaServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reservas")
public class ReservasControlador {

    @Autowired
    private ReservaServicios reservaServicios;

    // Ver todas las reservas
    @GetMapping("/verReservas")
    public String verReservas(Model model) {
        List<Reserva> reservas = reservaServicios.obtenerTodasLasReservas();
        model.addAttribute("reservas", reservas);
        return "verReservas"; // Renderiza la vista "verReservas.html"
    }

    // Consultar estado de una reserva y renderizar una vista
    @GetMapping("/reservaConfirmada/{id}")
    public String consultarEstadoReserva(@PathVariable Long id, Model model) {
        try {
            Reserva reserva = reservaServicios.obtenerReservaPorId(id);
            if (reserva != null) {
                model.addAttribute("reserva", reserva); // Cargar datos en el modelo
                return "estadoReserva"; // Retorna la vista Thymeleaf
            } else {
                model.addAttribute("error", "Reserva no encontrada para el ID: " + id);
                return "error"; // Vista Thymeleaf para errores
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al consultar la reserva: " + e.getMessage());
            return "error"; // Vista Thymeleaf para errores
        }
    }

    // Marcar reserva como entregada
    @PostMapping("/marcarEntregada/{id}")
    public String marcarReservaEntregada(@PathVariable Long id, Model model) {
        try {
            Reserva reserva = reservaServicios.obtenerReservaPorId(id);
            if (reserva != null) {
                reserva.setEstado("Entregado");
                reservaServicios.guardarReserva(reserva);
                model.addAttribute("mensaje", "Reserva marcada como entregada");
                model.addAttribute("reserva", reserva);
                return "estadoReserva"; // Renderiza la vista actualizada
            } else {
                model.addAttribute("error", "Reserva no encontrada");
                return "error"; // Renderiza la vista "error.html"
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al marcar la reserva como entregada: " + e.getMessage());
            return "error"; // Renderiza la vista "error.html"
        }
    }

    // Cancelar una reserva
    @PostMapping("/cancelar/{id}")
    public String cancelarReserva(@PathVariable Long id, Model model) {
        try {
            Reserva reserva = reservaServicios.obtenerReservaPorId(id);
            if (reserva != null) {
                reserva.setEstado("Cancelada");
                reservaServicios.guardarReserva(reserva);
                model.addAttribute("mensaje", "Reserva cancelada con éxito");
                model.addAttribute("reserva", reserva);
                return "estadoReserva"; // Renderiza la vista actualizada
            } else {
                model.addAttribute("error", "Reserva no encontrada");
                return "error"; // Renderiza la vista "error.html"
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al cancelar la reserva: " + e.getMessage());
            return "error"; // Renderiza la vista "error.html"
        }
    }

    @GetMapping("/consultarReservas")
    public String consultarReservasAdmin(Model model) {
        List<Reserva> reservas = reservaServicios.obtenerTodasLasReservas(); // Llamada al servicio para obtener todas las reservas
        model.addAttribute("reservas", reservas); // Añadir las reservas al modelo
        return "consultarReservas"; // Renderiza la vista consultarReservas.html
    }

}
