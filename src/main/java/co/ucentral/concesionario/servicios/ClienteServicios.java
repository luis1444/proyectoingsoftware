package co.ucentral.concesionario.servicios;

import co.ucentral.concesionario.persistencia.entidades.Cliente;
import co.ucentral.concesionario.persistencia.repositorios.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServicios {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepositorio.save(cliente);
    }

    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepositorio.findAll();
    }


    public Cliente obtenerClientePorId(Long id) {
        return clienteRepositorio.findById(id).orElse(null);
    }

    public Cliente obtenerClientePorEmail(String correoElectronico) {
        return  clienteRepositorio.findByCorreoElectronico(correoElectronico).orElse(null);
    }
}
