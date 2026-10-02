package com.api.alquilaya.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "vehiculo")
public class VehiculoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private Long idVehiculo;

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column(nullable = false)
    private Integer anio;

    @Column(nullable = false, unique = true, length = 20)
    private String matricula;

    @Column(name = "tarifa_diaria", nullable = false,
            precision = 10, scale = 2)
    private BigDecimal tarifaDiaria;

    @Column(nullable = false, length = 20)
    private String estado;

    // Cada vehículo pertenece a una oficina.
    @ManyToOne
    @JoinColumn(name = "id_oficina", nullable = false)
    private OficinaModel oficina;

    @OneToMany(mappedBy = "vehiculo",
            cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("vehiculo")
    private List<AlquilerModel> alquileres;

    public VehiculoModel() {
    }

    public VehiculoModel(String marca, String modelo, Integer anio,
                         String matricula, BigDecimal tarifaDiaria,
                         String estado, OficinaModel oficina) {
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.matricula = matricula;
        this.tarifaDiaria = tarifaDiaria;
        this.estado = estado;
        this.oficina = oficina;
    }

    public Long getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public BigDecimal getTarifaDiaria() {
        return tarifaDiaria;
    }

    public void setTarifaDiaria(BigDecimal tarifaDiaria) {
        this.tarifaDiaria = tarifaDiaria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public OficinaModel getOficina() {
        return oficina;
    }

    public void setOficina(OficinaModel oficina) {
        this.oficina = oficina;
    }

    public List<AlquilerModel> getAlquileres() {
        return alquileres;
    }

    public void setAlquileres(List<AlquilerModel> alquileres) {
        this.alquileres = alquileres;
    }
}