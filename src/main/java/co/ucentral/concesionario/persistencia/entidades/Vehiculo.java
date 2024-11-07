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
@Table(name = "vehiculo")
@Entity
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "veh_id")
    private Long id;

    @Column(name = "veh_marca", nullable = false)
    private String marca;

    @Column(name = "veh_modelo", nullable = false)
    private String modelo;

    @Column(name = "veh_anio", nullable = false)
    private int anio;

    @Column(name = "veh_color", nullable = false)
    private String color;

    @Column(name = "veh_precio", nullable = false)
    private double precio;

    @Column(name = "veh_tipo_combustible", nullable = false)
    private String tipoCombustible;

    @Column(name = "veh_pais_origen", nullable = false)
    private String paisOrigen;

    @Lob
    @Column(name = "veh_foto")
    private byte[] foto;
}
