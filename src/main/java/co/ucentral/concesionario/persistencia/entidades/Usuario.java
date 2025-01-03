package co.ucentral.concesionario.persistencia.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "usuario")
@Entity
public class Usuario {

    @Id
    @Column(name = "usu_usuario")
    public String usuario;


    @Column(name = "usu_contrasena", nullable = false)
    private String contrasena;

    @Column(name = "usu_rol")
    public String rol;
}
