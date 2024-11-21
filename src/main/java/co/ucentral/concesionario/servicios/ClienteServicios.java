package co.ucentral.concesionario.servicios;

import co.ucentral.concesionario.persistencia.entidades.Cliente;
import co.ucentral.concesionario.persistencia.repositorios.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServicios {

    ClienteRepositorio clienteRepositorio;


    public Cliente registrarReserva(Cliente cliente) {
        return clienteRepositorio.save(cliente);
    }
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepositorio.findAll();
    }




}
