package co.ucentral.concesionario.controladores;

import co.ucentral.concesionario.persistencia.entidades.Vehiculo;
import co.ucentral.concesionario.servicios.VehiculoServicios;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@AllArgsConstructor
@Controller
public class VehiculoControlador {

    private final VehiculoServicios vehiculoServicios;

    @GetMapping("/registroVehiculo")
    public String mostrarFormularioDeRegistroVehiculo(Model model) {
        Vehiculo vehiculo = new Vehiculo(); // Crear un objeto de Vehiculo vacío
        model.addAttribute("vehiculo", new Vehiculo());
        return "registroVehiculos"; // Vista donde se registra un vehículo
    }

    @PostMapping("/registroVehiculo")
    public String registrarVehiculo(@ModelAttribute Vehiculo vehiculo, Model model) {
        vehiculoServicios.registrarVehiculo(vehiculo); // Llamar al servicio que registra el vehículo
        model.addAttribute("mensaje", "Vehículo registrado con éxito");
        return "pantallaFabricante"; // Volver a la pantalla del fabricante después del registro
    }
}
