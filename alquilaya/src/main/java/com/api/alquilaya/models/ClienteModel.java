package com.api.alquilaya.models;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonCreator;

@Entity
@Table(name = "cliente")
public class ClienteModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "dni", length = 9, unique = true)
    private String dni;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 50)
    private String apellido;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "licencia_conducir", nullable = false,
            unique = true, length = 50)
    private String licenciaConducir;

    @Column(length = 20)
    private String telefono;

    @OneToMany(mappedBy = "cliente",
            cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("cliente")
    private List<AlquilerModel> alquileres;

    @JsonCreator
    public ClienteModel() {
    }

    public ClienteModel(String dni,
                        String nombre,
                        String apellido,
                        String email,
                        String licenciaConducir,
                        String telefono) {
        validarDni(dni);
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.licenciaConducir = licenciaConducir;
        this.telefono = telefono;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        validarDni(dni);
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicenciaConducir() {
        return licenciaConducir;
    }

    public void setLicenciaConducir(String licenciaConducir) {
        this.licenciaConducir = licenciaConducir;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<AlquilerModel> getAlquileres() {
        return alquileres;
    }

    public void setAlquileres(List<AlquilerModel> alquileres) {
        this.alquileres = alquileres;
    }

    private void validarDni(String dni) {
        if (dni == null || dni.length() != 9) {
            throw new IllegalArgumentException(
                    "El DNI debe tener exactamente 9 caracteres");
        }
    }
}

