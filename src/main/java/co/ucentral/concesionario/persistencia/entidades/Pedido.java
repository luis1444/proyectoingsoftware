package co.ucentral.concesionario.persistencia.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "pedidos")
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ped_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "veh_id", nullable = false) // Relación con la entidad Vehiculo
    private Vehiculo vehiculo;

    @Column(name = "ped_cliente", nullable = false)
    private String cliente;

    @Column(name = "ped_cantidad", nullable = false)
    private int cantidad;

    @Column(name = "ped_fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "ped_estado", nullable = false)
    private String estado;


    @Column(name = "ped_pais", nullable = false) // Asegúrate de que esta columna exista en tu base de datos
    private String pais;
}