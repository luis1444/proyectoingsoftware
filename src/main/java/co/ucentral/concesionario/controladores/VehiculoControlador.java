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

    // Método para mostrar el formulario de registro
    @GetMapping("/registroVehiculo")
    public String mostrarFormularioDeRegistroVehiculo(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        return "registroVehiculos";
    }

    // Registrar vehículo (POST)
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

        // Guardar la foto del vehículo
        if (!foto.isEmpty()) {
            try {
                byte[] fotoBytes = foto.getBytes();
                vehiculo.setFoto(fotoBytes);
            } catch (IOException e) {
                model.addAttribute("errorMessage", "Error al procesar la foto.");
                return "error";
            }
        } else {
            model.addAttribute("errorMessage", "No se ha cargado ninguna foto.");
            return "error";
        }

        // Registrar el vehículo en la base de datos
        try {
            vehiculoServicios.registrarVehiculo(vehiculo);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al guardar el vehículo: " + e.getMessage());
            return "error";
        }

        return "redirect:/pantallaFabricante";
    }

    // Obtener todos los vehículos (para API)
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

    // Obtener vehículo por ID (para modificar)
    @GetMapping("/api/vehiculos/{id}")
    @ResponseBody
    public ResponseEntity<Vehiculo> obtenerVehiculo(@PathVariable Long id) {
        Vehiculo vehiculo = vehiculoServicios.obtenerPorId(id);
        if (vehiculo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vehiculo);
    }

    // Actualizar un vehículo
    @PutMapping("/api/vehiculos/{id}")
    @ResponseBody
    public ResponseEntity<String> actualizarVehiculo(@PathVariable Long id, @RequestBody Vehiculo vehiculo) {
        Vehiculo vehiculoExistente = vehiculoServicios.obtenerPorId(id);
        if (vehiculoExistente == null) {
            return ResponseEntity.notFound().build();
        }

        vehiculo.setId(id); // Aseguramos que se actualice el vehículo con el ID especificado
        vehiculoServicios.actualizarVehiculo(vehiculo);
        return ResponseEntity.ok("Vehículo actualizado");
    }

    @PostMapping("/modificarVehiculo")
    public String modificarVehiculo(@RequestParam Long id,
                                    @RequestParam String marca,
                                    @RequestParam String modelo,
                                    @RequestParam int anio,
                                    @RequestParam String color,
                                    @RequestParam double precio,
                                    @RequestParam String tipoCombustible,
                                    @RequestParam String paisOrigen,
                                    Model model) {

        Vehiculo vehiculo = vehiculoServicios.obtenerPorId(id);
        if (vehiculo == null) {
            model.addAttribute("errorMessage", "Vehículo no encontrado.");
            return "error";
        }

        // Actualiza los datos del vehículo
        vehiculo.setMarca(marca);
        vehiculo.setModelo(modelo);
        vehiculo.setAnio(anio);
        vehiculo.setColor(color);
        vehiculo.setPrecio(precio);
        vehiculo.setTipoCombustible(tipoCombustible);
        vehiculo.setPaisOrigen(paisOrigen);

        vehiculoServicios.actualizarVehiculo(vehiculo);

        return "redirect:/pantallaFabricante";  // Redirige al listado o a otra página de éxito
    }

    // Eliminar un vehículo
    @DeleteMapping("/eliminarVehiculo/{id}")
    @ResponseBody
    public ResponseEntity<String> eliminarVehiculo(@PathVariable Long id) {
        Vehiculo vehiculo = vehiculoServicios.obtenerPorId(id);
        if (vehiculo == null) {
            return ResponseEntity.notFound().build();  // Vehículo no encontrado
        }

        vehiculoServicios.borrar(vehiculo);  // Eliminar el vehículo
        return ResponseEntity.ok("Vehículo eliminado");  // Confirmar que se eliminó
    }

    // Página de eliminación (POST)
    @PostMapping("/eliminarVehiculo")
    public String eliminarVehiculoPost(@RequestParam Long id) {
        Vehiculo vehiculo = vehiculoServicios.obtenerPorId(id);
        if (vehiculo != null) {
            vehiculoServicios.borrar(vehiculo);
        }
        return "redirect:/vehiculos";  // Redirige al listado de vehículos
    }

    @GetMapping("/modificarVehiculo")
    public String mostrarModificarVehiculo() {
        return "modificarVehiculo"; // Asegúrate de tener un archivo .html correspondiente en resources/templates
    }
}