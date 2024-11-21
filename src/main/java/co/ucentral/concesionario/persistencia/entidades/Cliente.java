package co.ucentral.concesionario.persistencia.entidades;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cliente {
    @Id
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
