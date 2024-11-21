package co.ucentral.concesionario.controladores;

import co.ucentral.concesionario.persistencia.entidades.Reserva;
import co.ucentral.concesionario.servicios.ReservaServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        System.out.println("Reservas obtenidas: " + reservas);
        model.addAttribute("reservas", reservas);
        return "verReservas";
    }

}