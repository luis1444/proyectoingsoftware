package co.ucentral.concesionario.controladores;

import co.ucentral.concesionario.persistencia.entidades.Vehiculo;
import co.ucentral.concesionario.servicios.VehiculoServicios;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public String registrarVehiculo(@ModelAttribute Vehiculo vehiculo, @RequestParam("foto") MultipartFile foto) {
        if (!foto.isEmpty()) {
            try {
                // Convertir la imagen a un arreglo de bytes
                byte[] fotoBytes = foto.getBytes();
                // Asignar la foto al vehículo
                vehiculo.setFoto(fotoBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Si no se carga foto, manejarlo según sea necesario
            System.out.println("No se ha cargado ninguna foto.");
        }
        vehiculoServicios.registrarVehiculo(vehiculo);
        return "redirect:/pantallaFabricante";
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
