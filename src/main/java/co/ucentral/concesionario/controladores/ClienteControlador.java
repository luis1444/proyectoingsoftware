package co.ucentral.concesionario.controladores;

import co.ucentral.concesionario.persistencia.entidades.Cliente;
import co.ucentral.concesionario.persistencia.entidades.Reserva;
import co.ucentral.concesionario.persistencia.entidades.Vehiculo;
import co.ucentral.concesionario.servicios.ClienteServicios;
import co.ucentral.concesionario.servicios.ReservaServicios;
import co.ucentral.concesionario.servicios.VehiculoServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@Controller
@RequestMapping("/cliente")
public class ClienteControlador {

    @Autowired
    private ClienteServicios clienteServicios;

    @Autowired
    private VehiculoServicios vehiculoServicios;

    @Autowired
    private ReservaServicios reservaServicios;

    // Mostrar formulario de reserva con el vehículo seleccionado
    @GetMapping("/reservar")
    public String mostrarFormularioReserva(@RequestParam String vehiculo, Model model) {
        System.out.println("Vehículo recibido en GET: " + vehiculo);  // Depuración
        model.addAttribute("vehiculo", vehiculo); // Pasamos el nombre del vehículo al modelo
        model.addAttribute("cliente", new Cliente());
        return "reservarCliente"; // Nombre del archivo en templates/
    }

    // Registrar la reserva desde el formulario
    @PostMapping("/registrar")
    public String registrarReserva(
            @ModelAttribute Cliente cliente, // Cliente es recibido automáticamente por Spring
            @RequestParam String vehiculo,  // Recibimos el nombre del vehículo desde el formulario
            @RequestParam String paymentMethod,
            @RequestParam String deliveryDate,
            Model model) {
        try {
            System.out.println("Datos recibidos en POST (Cliente): " + cliente);  // Depuración
            System.out.println("Vehículo recibido en POST (Nombre): " + vehiculo);  // Depuración

            // Validar y obtener el vehículo por nombre
            Vehiculo vehiculoSeleccionado = vehiculoServicios.obtenerVehiculoPorNombre(vehiculo);
            if (vehiculoSeleccionado == null) {
                model.addAttribute("error", "El vehículo no existe.");
                return "reservarCliente";
            }

            // Validar si el cliente existe o crearlo
            Cliente clienteExistente = clienteServicios.obtenerClientePorEmail(cliente.getCorreoElectronico());
            if (clienteExistente == null) {
                clienteServicios.guardarCliente(cliente);  // Guardamos el cliente si no existe
            } else {
                cliente = clienteExistente; // Usamos el cliente existente
            }

            // Crear la reserva
            Reserva nuevaReserva = new Reserva();
            nuevaReserva.setCliente(cliente);
            nuevaReserva.setVehiculo(vehiculoSeleccionado);
            nuevaReserva.setEstado("Pendiente");
            nuevaReserva.setFechaReserva(new Date(System.currentTimeMillis())); // Fecha actual
            nuevaReserva.setFechaInicio(Date.valueOf(deliveryDate));
            nuevaReserva.setFechaFin(Date.valueOf(deliveryDate)); // Ajusta según lógica de negocio

            // Guardar la reserva
            Reserva reservaGuardada = reservaServicios.guardarReserva(nuevaReserva);

            // Redirigir a la vista de confirmación
            return "redirect:/cliente/reservaConfirmada/" + reservaGuardada.getId();
        } catch (Exception e) {
            // Manejo de errores
            model.addAttribute("error", "Error al registrar la reserva: " + e.getMessage());
            return "reservarCliente"; // Volver al formulario en caso de error
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
