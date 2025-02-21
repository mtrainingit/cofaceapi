package com.coface.usuario.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(
        name = "tareas"
)
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tareas_id_seq")
    @SequenceGenerator(name = "tareas_id_seq", sequenceName = "tareas_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @ManyToOne
    @JoinColumn(
            name = "usuario_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "usuario_tarea_fk"
            )
    )
    @JsonIgnore
    private Usuario usuario;

    public Tarea() {
    }

    public Tarea(Long id, String nombre, String descripcion, Usuario usuario) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.usuario = usuario;
    }

    public Tarea(String nombre, String descripcion, Usuario usuario) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.usuario = usuario;
    }

    public Tarea(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @JsonIgnore
    public String getUsuario() {
        return usuario.getNombre();
    }

    @JsonIgnore
    public Long getUsuarioId() {
        return usuario.getId();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
