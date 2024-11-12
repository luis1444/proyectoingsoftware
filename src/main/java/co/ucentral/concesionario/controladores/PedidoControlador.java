package co.ucentral.concesionario.controladores;

import co.ucentral.concesionario.persistencia.entidades.Pedido;
import co.ucentral.concesionario.persistencia.entidades.Vehiculo;
import co.ucentral.concesionario.servicios.PedidoServicios;
import co.ucentral.concesionario.servicios.VehiculoServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class PedidoControlador {

    @Autowired
    private PedidoServicios pedidoServicios;

    @Autowired
    private VehiculoServicios vehiculoServicios;

    @PostMapping("/realizarPedido")
    public String registrarPedido(@RequestParam Long vehiculoId,
                                  @RequestParam int cantidad,
                                  @RequestParam String cliente,
                                  @RequestParam String pais) { // Agregar el parámetro 'pais'
        Vehiculo vehiculo = vehiculoServicios.obtenerPorId(vehiculoId);
        if (vehiculo != null) {
            // Crear un nuevo objeto Pedido y establecer sus propiedades
            Pedido pedido = new Pedido();
            pedido.setVehiculo(vehiculo);
            pedido.setCantidad(cantidad);
            pedido.setCliente(cliente);
            pedido.setPais(pais); // Establecer el país
            pedido.setFecha(LocalDate.now()); // Establecer la fecha actual
            pedido.setEstado("Pendiente"); // Establecer un estado inicial

            pedidoServicios.registrarPedido(pedido); // Asegúrate de que este método acepte un objeto Pedido
            return "redirect:/pedidos"; // Redirigir a la página de pedidos
        } else {
            throw new RuntimeException("Vehículo no encontrado con ID: " + vehiculoId);
        }
    }

    @GetMapping
    public String obtenerTodos(Model model) {
        List<Pedido> pedidos = pedidoServicios.obtenerTodos();
        model.addAttribute("pedidos", pedidos);
        return "pedidos"; // Nombre de la plantilla Thymeleaf para mostrar los pedidos
    }

    @GetMapping("/realizarPedido")
    public String mostrarRealizarPedido(Model model) {
        List<Vehiculo> vehiculos = vehiculoServicios.obtenerTodos(); // Cargar vehículos para el formulario
        model.addAttribute("vehiculos", vehiculos);
        model.addAttribute("pedido", new Pedido()); // Crear un nuevo objeto Pedido
        return "realizarPedido"; // Asegúrate de que este nombre coincida con tu plantilla
    }

    @PutMapping("/{id}")
    public String actualizarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        pedidoServicios.actualizarEstado(id, nuevoEstado);
        return "redirect:/pedidos"; // Redirigir a la página de pedidos actualizada
    }

    @DeleteMapping("/{id}")
    public String borrarPedido(@PathVariable Long id) {
        pedidoServicios.borrarPedido(id);
        return "redirect:/pedidos";
    }
}