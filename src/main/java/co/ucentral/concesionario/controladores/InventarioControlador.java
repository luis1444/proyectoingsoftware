package co.ucentral.concesionario.controladores;

import co.ucentral.concesionario.persistencia.entidades.Inventario;
import co.ucentral.concesionario.persistencia.entidades.Pedido;
import co.ucentral.concesionario.persistencia.entidades.Vehiculo;
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

     InventarioServicios inventarioServicios;
     PedidoServicios pedidoServicios;


    @PostMapping
    public ResponseEntity<String> registrarInventario(@RequestBody Inventario inventario) {
        try {
            inventarioServicios.registrarInventario(inventario);
            return ResponseEntity.ok("Inventario registrado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al registrar inventario: " + e.getMessage());
        }
    }

    // Obtener todos los vehículos en el inventario
    @GetMapping
    public ResponseEntity<List<Inventario>> obtenerTodos() {
        List<Inventario> inventarios = inventarioServicios.obtenerTodos();
        return ResponseEntity.ok(inventarios);
    }

    // Obtener un vehículo específico en el inventario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Inventario> obtenerPorId(@PathVariable Long id) {
        Inventario inventario = inventarioServicios.obtenerPorId(id);
        if (inventario != null) {
            return ResponseEntity.ok(inventario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar la cantidad de un vehículo en el inventario
    @PutMapping("/{id}/cantidad")
    public ResponseEntity<String> actualizarCantidadInventario(@PathVariable Long id, @RequestParam int cantidad) {
        try {
            inventarioServicios.actualizarCantidadInventario(id, cantidad);
            return ResponseEntity.ok("Cantidad actualizada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar cantidad en inventario: " + e.getMessage());
        }
    }

    // Vender un vehículo, restando su cantidad del inventario
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
            // Lógica para entregar el pedido y actualizar el inventario
            Pedido pedido = pedidoServicios.obtenerPorId(id);
            if (pedido == null) {
                return ResponseEntity.status(404).body("Pedido no encontrado.");
            }

            if (pedido.getEstado().equals("Entregado")) {
                return ResponseEntity.status(400).body("El pedido ya ha sido entregado.");
            }

            Vehiculo vehiculo = pedido.getVehiculo();
            int cantidadPedida = pedido.getCantidad();

            Inventario inventario = inventarioServicios.obtenerPorVehiculoId(vehiculo.getId());
            if (inventario == null) {
                inventario = new Inventario();
                inventario.setVehiculo(vehiculo);
                inventario.setCantidad(cantidadPedida);
                inventarioServicios.registrarInventario(inventario);
            } else {
                inventario.setCantidad(inventario.getCantidad() + cantidadPedida);
                inventarioServicios.actualizarCantidadInventario(inventario.getId(), inventario.getCantidad());
            }

            pedidoServicios.entregarPedido(id);

            return ResponseEntity.ok("Pedido entregado y actualizado en inventario.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al entregar el pedido: " + e.getMessage());
        }
    }

    @GetMapping("/pedidos")
    public String mostrarPedidos(Model model) {
        List<Pedido> pedidos = pedidoServicios.obtenerTodos();
        System.out.println("Pedidos recuperados: " + pedidos); // Verifica datos
        model.addAttribute("pedidos", pedidos);
        return "verInventario"; // Nombre del archivo HTML
    }

    @GetMapping("/verInventario")
    public String mostrarInventario(Model model) {
        try {
            List<Inventario> inventarios = inventarioServicios.obtenerTodos();
            List<Pedido> pedidosPendientes = pedidoServicios.obtenerTodos(); // Ajustar lógica según necesidad

            model.addAttribute("inventarios", inventarios);
            model.addAttribute("pedidosPendientes", pedidosPendientes);

            return "verInventario";
        } catch (Exception e) {
            model.addAttribute("error", "Error al cargar inventario y pedidos: " + e.getMessage());
            return "error"; // Renderiza una página de error personalizada
        }
    }

}
