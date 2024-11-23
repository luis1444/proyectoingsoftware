package co.ucentral.concesionario.controladores;

import co.ucentral.concesionario.persistencia.entidades.Inventario;
import co.ucentral.concesionario.persistencia.entidades.Pedido;
import co.ucentral.concesionario.servicios.InventarioServicios;
import co.ucentral.concesionario.servicios.PedidoServicios;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/inventario")
public class InventarioControlador {

    private final InventarioServicios inventarioServicios;
    private final PedidoServicios pedidoServicios;

    @PostMapping
    public ResponseEntity<String> registrarInventario(@RequestBody Inventario inventario) {
        try {
            inventarioServicios.registrarInventario(inventario);
            return ResponseEntity.ok("Inventario registrado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al registrar inventario: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Inventario>> obtenerTodos() {
        return ResponseEntity.ok(inventarioServicios.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> obtenerPorId(@PathVariable Long id) {
        Inventario inventario = inventarioServicios.obtenerPorId(id);
        return inventario != null
                ? ResponseEntity.ok(inventario)
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/cantidad")
    public ResponseEntity<String> actualizarCantidadInventario(@PathVariable Long id, @RequestParam int cantidad) {
        try {
            inventarioServicios.actualizarCantidadInventario(id, cantidad);
            return ResponseEntity.ok("Cantidad actualizada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar cantidad en inventario: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/vender")
    public ResponseEntity<String> venderVehiculo(@PathVariable Long id, @RequestParam int cantidad) {
        try {
            inventarioServicios.venderVehiculo(id, cantidad);
            return ResponseEntity.ok("Venta realizada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al realizar venta: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/entregar")
    @Transactional
    public ResponseEntity<String> entregarPedido(@PathVariable Long id) {
        try {
            pedidoServicios.entregarPedido(id);
            return ResponseEntity.ok("Pedido entregado y actualizado en inventario.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al entregar el pedido: " + e.getMessage());
        }
    }

    @GetMapping("/pedidos")
    public String mostrarPedidos(Model model) {
        model.addAttribute("pedidos", pedidoServicios.obtenerTodos());
        return "verInventario"; // Renderiza la vista correspondiente
    }

    @GetMapping("/verInventario")
    public String mostrarInventario(Model model) {
        try {
            model.addAttribute("inventarios", inventarioServicios.obtenerTodos());
            model.addAttribute("pedidosPendientes", pedidoServicios.obtenerPedidosPendientes());
            return "verInventario";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar inventario y pedidos: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/aceptarPedido")
    @Transactional
    public String aceptarPedido(@RequestParam Long pedidoId, Model model) {
        try {
            pedidoServicios.entregarPedido(pedidoId);
            model.addAttribute("mensaje", "Pedido aceptado y actualizado en el inventario.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", "Error al aceptar el pedido: " + e.getMessage());
        }
        return "redirect:/inventario/verInventario";
    }
}
