package co.ucentral.concesionario.controladores;

import co.ucentral.concesionario.persistencia.entidades.Inventario;
import co.ucentral.concesionario.persistencia.entidades.Pedido;
import co.ucentral.concesionario.persistencia.entidades.Vehiculo;
import co.ucentral.concesionario.servicios.InventarioServicios;
import co.ucentral.concesionario.servicios.PedidoServicios;
import co.ucentral.concesionario.servicios.VehiculoServicios;
import jakarta.transaction.Transactional;
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

    InventarioServicios inventarioServicios;

    // Registrar un nuevo pedido
    @PostMapping("/registrarPedido")
    public String registrarPedido(@RequestParam Long vehiculoId,
                                  @RequestParam int cantidad,
                                  @RequestParam String pais,
                                  Model model) {
        try {
            pedidoServicios.registrarPedido(vehiculoId, cantidad, pais);
            model.addAttribute("mensaje", "Pedido registrado exitosamente.");
            return "pedido_exitoso"; // Página de éxito
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar el pedido: " + e.getMessage());
            return "pedido_error"; // Página de error
        }
    }

    // Mostrar todos los pedidos
    @GetMapping
    public String obtenerTodos(Model model) {
        try {
            List<Pedido> pedidos = pedidoServicios.obtenerTodos();
            model.addAttribute("pedidos", pedidos != null ? pedidos : List.of());
            return "pedidos";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar los pedidos: " + e.getMessage());
            return "error";
        }
    }

    // Mostrar formulario para realizar un pedido
    @GetMapping("/realizarPedido")
    public String mostrarRealizarPedido(Model model) {
        try {
            List<Vehiculo> vehiculos = vehiculoServicios.obtenerTodos();
            model.addAttribute("vehiculos", vehiculos != null ? vehiculos : List.of());
            return "realizarPedido"; // Asegúrate de que esta plantilla existe
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar vehículos: " + e.getMessage());
            return "error";
        }
    }

    // Consultar pedidos
    @GetMapping("/consultarPedidos")
    public String mostrarPedidos(Model model) {
        try {
            List<Pedido> pedidos = pedidoServicios.obtenerTodos();
            model.addAttribute("pedidos", pedidos != null ? pedidos : List.of());
            return "consultarPedidos";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar pedidos: " + e.getMessage());
            return "error";
        }
    }

    // Mostrar pedidos pendientes para entregar

    @GetMapping("/entregarPedidos")
    @Transactional // Necesario para manejar FetchType.LAZY
    public String mostrarEntregarPedidos(Model model) {
        try {
            List<Pedido> pedidosPendientes = pedidoServicios.obtenerPedidosPorEstado("Pendiente");
            if (pedidosPendientes == null || pedidosPendientes.isEmpty()) {
                model.addAttribute("mensaje", "No hay pedidos pendientes para entregar.");
            } else {
                model.addAttribute("pedidos", pedidosPendientes);
            }
            return "entregarPedidos"; // Asegúrate de que esta plantilla exista
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar pedidos pendientes: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/entregar")
    @Transactional
    public String entregarPedido(@RequestParam Long pedidoId, Model model) {
        try {
            pedidoServicios.entregarPedido(pedidoId);
            model.addAttribute("mensaje", "Pedido entregado con éxito.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al entregar el pedido: " + e.getMessage());
        }
        return "redirect:/pedidos/entregarPedidos"; // Redirige para recargar la lista actualizada
    }

    @PostMapping("/aceptarPedido")
    @Transactional
    public String aceptarPedido(@RequestParam Long pedidoId, Model model) {
        try {
            Pedido pedido = pedidoServicios.obtenerPorId(pedidoId);
            if (pedido == null || !pedido.getEstado().equals("Pendiente")) {
                model.addAttribute("error", "El pedido no es válido o ya ha sido procesado.");
                return "redirect:/pedidos/inventario";
            }

            Vehiculo vehiculo = pedido.getVehiculo();
            int cantidadPedida = pedido.getCantidad();

            // Actualizar el inventario
            Inventario inventario = inventarioServicios.obtenerPorVehiculoId(vehiculo.getId());
            if (inventario == null) {
                inventario = new Inventario();
                inventario.setVehiculo(vehiculo);
                inventario.setCantidad(cantidadPedida);
                inventarioServicios.registrarInventario(inventario);
            } else {
                inventario.setCantidad(inventario.getCantidad() + cantidadPedida);
                inventarioServicios.actualizarCantidadInventario(inventario.getId(), cantidadPedida);
            }

            // Cambiar estado del pedido
            pedidoServicios.entregarPedido(pedidoId);

            model.addAttribute("mensaje", "Pedido aceptado y actualizado en el inventario.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al aceptar el pedido: " + e.getMessage());
        }
        return "redirect:/pedidos/inventario";
    }



}
