package co.ucentral.concesionario.controladores;

import co.ucentral.concesionario.persistencia.entidades.Vehiculo;
import co.ucentral.concesionario.servicios.VehiculoServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteControlador {

    @Autowired
    private VehiculoServicios vehiculoServicios;

    @GetMapping("/vehiculos")
    public String mostrarVehiculos(Model model) {
        List<Vehiculo> vehiculos = vehiculoServicios.obtenerTodosLosVehiculos(); // Método para obtener todos los vehículos
        model.addAttribute("vehiculos");
        return "cliente/vehiculosCliente"; // La plantilla Thymeleaf
    }
}