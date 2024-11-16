package co.ucentral.concesionario.controladores;

import co.ucentral.concesionario.persistencia.entidades.Cliente;
import co.ucentral.concesionario.persistencia.entidades.Vehiculo;
import co.ucentral.concesionario.servicios.ClienteServicios;
import co.ucentral.concesionario.servicios.VehiculoServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteControlador {

    @Autowired
    private ClienteServicios clienteServicios;

    @PostMapping("/registrar")
    public String registrarReserva(@ModelAttribute Cliente cliente, Model model) {
        Cliente clienteGuardado = clienteServicios.registrarReserva(cliente);
        model.addAttribute("cliente", clienteGuardado);
        return "redirect:reservaConfirmadescargar.html  " + clienteGuardado.getId();
    }
    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteServicios.obtenerTodosLosClientes());
        return "listaClientes"; // Vista para mostrar la lista de clientes
    }

}
