package com.coface.usuario.db.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@NamedStoredProcedureQuery
(
        name = "Usuario.insertarUsuarioNamedQuery",
        procedureName = "insertar_usuario",
        parameters = {
                @StoredProcedureParameter(name = "p_nombre", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_email", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_password", mode = ParameterMode.IN, type = String.class),
                @StoredProcedureParameter(name = "p_rol", mode = ParameterMode.IN, type = Integer.class),
                @StoredProcedureParameter(name = "p_id", mode = ParameterMode.OUT, type = Long.class)
        },
        resultClasses = Long.class
)
@Table(
        name = "usuarios",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "usuarios_email_unique",
                        columnNames = "email"
                )
        }
)
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarios_id_seq")
    @SequenceGenerator(name = "usuarios_id_seq", sequenceName = "usuarios_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "rol", nullable = false)
    private Integer rol;

    @OneToOne(
            mappedBy = "usuario",
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private Direccion direccion;

    @OneToMany(
            mappedBy = "usuario",
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Tarea> tareas = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(Long id, String nombre, String email, String password, Integer rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    public Usuario(String nombre, String email, String password, Integer rol) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rol == 1 ? "ROLE_ADMIN" : "ROLE_USER"));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRol() {
        return rol;
    }

    public void setRol(Integer rol) {
        this.rol = rol;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public List<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    public void asignaTarea(Tarea tarea) {
        if (!tareas.contains(tarea)) {
            tareas.add(tarea);
        }
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", rol=" + rol +
                '}';
    }

}
