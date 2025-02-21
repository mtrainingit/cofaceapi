package com.coface.usuario.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(
        name = "direcciones",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "direcciones_usuario_id_unique",
                        columnNames = "usuario_id"
                )
        }
)
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "direcciones_id_seq")
    @SequenceGenerator(name = "direcciones_id_seq", sequenceName = "direcciones_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    @Column(name = "codigo_postal", nullable = false)
    private String codigoPostal;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "usuario_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "usuario_direccion_fk"
            )
    )
    @JsonIgnore
    private Usuario usuario;

    public Direccion() {
    }

    public Direccion(Long id, String direccion, String codigoPostal, Usuario usuario) {
        this.id = id;
        this.direccion = direccion;
        this.codigoPostal = codigoPostal;
        this.usuario = usuario;
    }

    public Direccion(String direccion, String codigoPostal, Usuario usuario) {
        this.direccion = direccion;
        this.codigoPostal = codigoPostal;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    @JsonIgnore
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Direccion{" +
                "id=" + id +
                ", direccion='" + direccion + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                '}';
    }
}
