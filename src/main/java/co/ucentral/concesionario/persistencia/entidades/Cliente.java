package co.ucentral.concesionario.persistencia.entidades;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class Cliente {
    private Long id;
    private String vehicleId;
    private String nombreCliente;
    private String apellidoCliente;
    private String correoElectronico;
    private String numeroTelefono;
    private String direccionEntrega;
    private String formaPago;
    private LocalDate fechaEntregaDeseada;

}
