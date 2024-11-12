package co.ucentral.concesionario.controladores;

import co.ucentral.concesionario.persistencia.entidades.Pedido;
import co.ucentral.concesionario.persistencia.entidades.Vehiculo;
import co.ucentral.concesionario.servicios.PedidoServicios;
import co.ucentral.concesionario.servicios.VehiculoServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class PedidoControlador {

    @Autowired
    private PedidoServicios pedidoServicios;

    @Autowired
    private VehiculoServicios vehiculoServicios;

    @PostMapping
    public String registrarPedido(@RequestParam Long vehiculoId, @RequestParam int cantidad) {
        Vehiculo vehiculo = vehiculoServicios.obtenerPorId(vehiculoId);
        if (vehiculo != null) {
            pedidoServicios.registrarPedido(vehiculo, cantidad);
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

    @GetMapping("/vehiculos")
    public String obtenerVehiculos(Model model) {
        List<Vehiculo> vehiculos = vehiculoServicios.obtenerTodos();
        model.addAttribute("vehiculos", vehiculos);
        return "vehiculos"; // Plantilla Thymeleaf para mostrar los vehículos disponibles
    }

    @GetMapping("/realizarPedido")
    public String mostrarRealizarPedido() {
        return "realizarPedido";
    }
}
