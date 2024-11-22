package co.ucentral.concesionario.controladores;

import co.ucentral.concesionario.persistencia.entidades.Cliente;
import co.ucentral.concesionario.persistencia.entidades.Reserva;
import co.ucentral.concesionario.persistencia.entidades.Vehiculo;
import co.ucentral.concesionario.servicios.ClienteServicios;
import co.ucentral.concesionario.servicios.ReservaServicios;
import co.ucentral.concesionario.servicios.VehiculoServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Map;

@Controller
@RequestMapping("/cliente")
public class ClienteControlador {

    @Autowired
    private ClienteServicios clienteServicios;

    @Autowired
    private VehiculoServicios vehiculoServicios;

    @Autowired
    private ReservaServicios reservaServicios;

    @GetMapping("/reservar")

    public String mostrarFormularioReserva(@RequestParam Long vehiculoId, Model model) {
        Vehiculo vehiculo = vehiculoServicios.obtenerPorId(vehiculoId);
        if (vehiculo == null) {
            model.addAttribute("error", "El vehículo especificado no existe.");
            return "error"; // Página de error si el vehículo no existe
        }

        model.addAttribute("vehiculo", vehiculo); // Pasa el objeto vehículo
        model.addAttribute("cliente", new Cliente());
        return "reservarCliente"; // Nombre del archivo en templates/
    }

    // Registrar la reserva desde el formulario
    @PostMapping("/registrar")
    @ResponseBody
    public ResponseEntity<?> registrarReserva(
            @ModelAttribute Cliente cliente,
            @RequestParam Long vehiculoId,
            @RequestParam String formaPago,
            @RequestParam String fechaEntregaDeseada) {
        try {
            Vehiculo vehiculoSeleccionado = vehiculoServicios.obtenerPorId(vehiculoId);
            if (vehiculoSeleccionado == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "El vehículo especificado no existe."));
            }

            cliente.setVehiculo(vehiculoSeleccionado);

            Cliente clienteExistente = clienteServicios.obtenerClientePorEmail(cliente.getCorreoElectronico());
            if (clienteExistente == null) {
                clienteServicios.guardarCliente(cliente);
            } else {
                cliente = clienteExistente;
            }

            Reserva nuevaReserva = new Reserva();
            nuevaReserva.setCliente(cliente);
            nuevaReserva.setVehiculo(vehiculoSeleccionado);
            nuevaReserva.setEstado("Pendiente");
            nuevaReserva.setFechaReserva(new Date(System.currentTimeMillis()));
            nuevaReserva.setFechaInicio(Date.valueOf(fechaEntregaDeseada));
            nuevaReserva.setFechaFin(Date.valueOf(fechaEntregaDeseada));

            Reserva reservaGuardada = reservaServicios.guardarReserva(nuevaReserva);

            // Enviar JSON completo con datos relevantes
            Map<String, Object> response = Map.of(
                    "reservaId", reservaGuardada.getId(),
                    "vehiculo", vehiculoSeleccionado.getMarca() + " " + vehiculoSeleccionado.getModelo(),
                    "precio", vehiculoSeleccionado.getPrecio(),
                    "cliente", cliente.getNombreCliente() + " " + cliente.getApellidoCliente(),
                    "fechaReserva", nuevaReserva.getFechaReserva().toString(),
                    "estado", nuevaReserva.getEstado()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al registrar la reserva: " + e.getMessage()));
        }
    }

    // Mostrar la confirmación de la reserva
    @GetMapping("/reservaConfirmada/{id}")
    public String mostrarReservaConfirmada(@PathVariable Long id, Model model) {
        try {
            Reserva reserva = reservaServicios.obtenerReservaPorId(id);
            model.addAttribute("reserva", reserva);
            return "reservaConfirmadadescargar"; // Vista de confirmación
        } catch (Exception e) {
            model.addAttribute("error", "No se pudo encontrar la reserva.");
            return "reservarCliente";
        }
    }
}