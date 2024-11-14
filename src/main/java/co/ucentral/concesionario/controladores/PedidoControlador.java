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

    @PostMapping("/registrarPedido")
    public String registrarPedido(@RequestParam Long vehiculoId,
                                  @RequestParam int cantidad,
                                  @RequestParam String pais,
                                  Model model) {
        try {
            pedidoServicios.registrarPedido(vehiculoId, cantidad, pais);
            model.addAttribute("mensaje", "Pedido registrado exitosamente");
            return "pedido_exitoso"; // Página de éxito
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar el pedido: " + e.getMessage());
            return "pedido_error"; // Página de error
        }
    }

    @GetMapping
    public String obtenerTodos(Model model) {
        List<Pedido> pedidos = pedidoServicios.obtenerTodos();
        model.addAttribute("pedidos", pedidos);
        return "pedidos";
    }

    @GetMapping("/realizarPedido")
    public String mostrarRealizarPedido(Model model) {
        List<Vehiculo> vehiculos = vehiculoServicios.obtenerTodos();
        model.addAttribute("vehiculos", vehiculos);
        return "realizarPedido"; // Asegúrate de que este nombre coincida con la plantilla HTML correcta
    }

    @GetMapping("/consultarPedidos")
    public String mostrarPedidos(Model model) {
        List<Pedido> pedidos = pedidoServicios.obtenerTodos();
        model.addAttribute("pedidos", pedidos);
        return "consultarPedidos"; // Nombre de la plantilla HTML que mostrarás
    }
}

