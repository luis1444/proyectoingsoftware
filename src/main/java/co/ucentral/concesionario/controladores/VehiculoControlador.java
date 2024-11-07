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
import java.util.Base64;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Controller
public class VehiculoControlador {

    private final VehiculoServicios vehiculoServicios;

    @GetMapping("/registroVehiculo")
    public String mostrarFormularioDeRegistroVehiculo(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        return "registroVehiculos";
    }

    @PostMapping("/registroVehiculo")
    public String registrarVehiculo(
            @RequestParam("marca") String marca,
            @RequestParam("modelo") String modelo,
            @RequestParam("anio") int anio,
            @RequestParam("color") String color,
            @RequestParam("precio") double precio,
            @RequestParam("tipoCombustible") String tipoCombustible,
            @RequestParam("paisOrigen") String paisOrigen,
            @RequestParam("foto") MultipartFile foto,
            Model model) {

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setMarca(marca);
        vehiculo.setModelo(modelo);
        vehiculo.setAnio(anio);
        vehiculo.setColor(color);
        vehiculo.setPrecio(precio);
        vehiculo.setTipoCombustible(tipoCombustible);
        vehiculo.setPaisOrigen(paisOrigen);

        if (!foto.isEmpty()) {
            try {
                // Convertir el archivo MultipartFile a byte[] y asignarlo a la propiedad foto
                byte[] fotoBytes = foto.getBytes();
                vehiculo.setFoto(fotoBytes);  // Asignamos el byte[] directamente al campo foto
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Error al procesar la foto");
                return "error";
            }
        } else {
            model.addAttribute("errorMessage", "No se ha cargado ninguna foto.");
            return "error";
        }

        try {
            // Guardar el vehículo en la base de datos
            vehiculoServicios.registrarVehiculo(vehiculo);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al guardar el vehículo: " + e.getMessage());
            return "error";
        }

        return "redirect:/pantallaFabricante";
    }
    @GetMapping("/api/vehiculos")
    @ResponseBody
    public List<Map<String, Object>> obtenerVehiculos() {
        List<Vehiculo> vehiculos = vehiculoServicios.obtenerTodos();
        return vehiculos.stream().map(vehiculo -> {
            Map<String, Object> vehiculoMap = Map.of(
                    "marca", vehiculo.getMarca(),
                    "modelo", vehiculo.getModelo(),
                    "anio", vehiculo.getAnio(),
                    "color", vehiculo.getColor(),
                    "precio", vehiculo.getPrecio(),
                    "tipoCombustible", vehiculo.getTipoCombustible(),
                    "paisOrigen", vehiculo.getPaisOrigen(),
                    "foto", vehiculo.getFoto() != null ? "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(vehiculo.getFoto()) : null
            );
            return vehiculoMap;
        }).toList();
    }

    // Endpoint para registrar vehículos en formato JSON
    @PostMapping("/api/vehiculos")
    @ResponseBody
    public ResponseEntity<Map<String, String>> registrarVehiculoAPI(@RequestBody Vehiculo vehiculo) {
        vehiculoServicios.registrarVehiculo(vehiculo);
        return ResponseEntity.ok(Map.of("success", "true"));
    }



}
