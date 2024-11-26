package co.ucentral.concesionario.persistencia.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "inventario")
@Entity
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inv_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "veh_id", referencedColumnName = "veh_id", nullable = false)
    private Vehiculo vehiculo; // Relación con la entidad Vehiculo

    @Column(name = "inv_cantidad", nullable = false)
    private int cantidad; // Cantidad en el inventario del concesionario

}
