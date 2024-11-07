package co.ucentral.concesionario.controladores;

import co.ucentral.concesionario.persistencia.entidades.Vehiculo;
import co.ucentral.concesionario.servicios.VehiculoServicios;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @ResponseBody // Asegúrate de que esta anotación esté presente para devolver solo el estado
    public ResponseEntity<?> registrarVehiculo(@ModelAttribute Vehiculo vehiculo) {
        vehiculoServicios.registrarVehiculo(vehiculo);
        return ResponseEntity.ok("Vehículo registrado con éxito");
    }
    @GetMapping("/api/vehiculos")
    @ResponseBody
    public List<Vehiculo> obtenerVehiculos() {
        return vehiculoServicios.obtenerTodos();
    }

    // Endpoint para registrar vehículos en formato JSON
    @PostMapping("/api/vehiculos")
    @ResponseBody
    public ResponseEntity<Map<String, String>> registrarVehiculoAPI(@RequestBody Vehiculo vehiculo) {
        vehiculoServicios.registrarVehiculo(vehiculo);
        return ResponseEntity.ok(Map.of("success", "true"));
    }
}
