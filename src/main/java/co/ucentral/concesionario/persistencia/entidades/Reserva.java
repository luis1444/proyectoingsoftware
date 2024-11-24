package co.ucentral.concesionario.persistencia.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "reserva")
@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "res_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;  // Relación con la entidad Cliente

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;  // Relación con la entidad Vehiculo

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "res_fecha_reserva", nullable = false)
    private Date fechaReserva;  // Fecha y hora de la reserva

    @Column(name = "res_estado", nullable = false)
    private String estado;  // Estado de la reserva (Ej. "Pendiente", "Confirmada", "Cancelada")

    @Column(name = "res_fecha_inicio", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;  // Fecha de inicio de la reserva

    @Column(name = "res_fecha_fin", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaFin;  // Fecha de fin de la reserva



}
