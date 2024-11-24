package co.ucentral.concesionario.persistencia.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cli_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @Column(name = "cli_nombre", nullable = false)
    private String nombreCliente;

    @Column(name = "cli_apellido", nullable = false)
    private String apellidoCliente;

    @Column(name = "cli_correo", nullable = false)
    private String correoElectronico;

    @Column(name = "cli_telefono", nullable = false)
    private String numeroTelefono;

    @Column(name = "cli_direccion", nullable = false)
    private String direccionEntrega;

    @Column(name = "cli_forma_pago", nullable = false)
    private String formaPago;

    @Column(name = "cli_fecha_entrega_deseada", nullable = false)
    private LocalDate fechaEntregaDeseada;
}
