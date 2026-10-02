package com.api.alquilaya.services;

import com.api.alquilaya.models.OficinaModel;
import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.repositories.OficinaRepository;
import com.api.alquilaya.repositories.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final OficinaRepository oficinaRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository,
                           OficinaRepository oficinaRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.oficinaRepository = oficinaRepository;
    }

    public List<VehiculoModel> listar() {
        return vehiculoRepository.findAll();
    }

    public VehiculoModel buscarPorId(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("El vehiculo no existe"));
    }

    @Transactional
    public VehiculoModel crear(VehiculoModel datos) {
        validar(datos);

        if ("Alquilado".equals(datos.getEstado())) {
            throw new IllegalArgumentException(
                    "El vehiculo debe registrarse Disponible o en Mantenimiento");
        }

        VehiculoModel vehiculo = new VehiculoModel();
        copiarDatos(vehiculo, datos);

        return vehiculoRepository.save(vehiculo);
    }

    @Transactional
    public VehiculoModel actualizar(Long id, VehiculoModel datos) {
        VehiculoModel vehiculo = buscarPorId(id);

        if ("Alquilado".equals(vehiculo.getEstado())) {
            throw new IllegalArgumentException(
                    "Primero debes completar el alquiler del vehiculo");
        }

        validar(datos);

        if ("Alquilado".equals(datos.getEstado())) {
            throw new IllegalArgumentException(
                    "El estado Alquilado se asigna al activar un alquiler");
        }

        copiarDatos(vehiculo, datos);

        return vehiculoRepository.save(vehiculo);
    }

    @Transactional
    public void eliminar(Long id) {
        VehiculoModel vehiculo = buscarPorId(id);

        if ("Alquilado".equals(vehiculo.getEstado())) {
            throw new IllegalArgumentException(
                    "No se puede eliminar un vehiculo alquilado");
        }

        if (vehiculo.getAlquileres() != null
                && !vehiculo.getAlquileres().isEmpty()) {
            throw new IllegalArgumentException(
                    "No se puede eliminar un vehiculo que tiene alquileres");
        }

        vehiculoRepository.delete(vehiculo);
    }

    private void copiarDatos(VehiculoModel vehiculo, VehiculoModel datos) {
        Long idOficina = datos.getOficina().getIdOficina();

        OficinaModel oficina = oficinaRepository.findById(idOficina)
                .orElseThrow(() ->
                        new IllegalArgumentException("La oficina no existe"));

        vehiculo.setMarca(datos.getMarca());
        vehiculo.setModelo(datos.getModelo());
        vehiculo.setAnio(datos.getAnio());
        vehiculo.setMatricula(datos.getMatricula());
        vehiculo.setTarifaDiaria(datos.getTarifaDiaria());
        vehiculo.setEstado(datos.getEstado());
        vehiculo.setOficina(oficina);
    }

    private void validar(VehiculoModel vehiculo) {
        if (vehiculo.getMarca() == null
                || vehiculo.getMarca().isBlank()) {
            throw new IllegalArgumentException(
                    "La marca es obligatoria");
        }

        if (vehiculo.getModelo() == null
                || vehiculo.getModelo().isBlank()) {
            throw new IllegalArgumentException(
                    "El modelo es obligatorio");
        }

        if (vehiculo.getAnio() == null || vehiculo.getAnio() <= 0) {
            throw new IllegalArgumentException(
                    "El anio debe ser mayor que cero");
        }

        if (vehiculo.getMatricula() == null
                || vehiculo.getMatricula().isBlank()) {
            throw new IllegalArgumentException(
                    "La matricula es obligatoria");
        }

        if (vehiculo.getTarifaDiaria() == null
                || vehiculo.getTarifaDiaria().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "La tarifa diaria debe ser mayor que cero");
        }

        if (!"Disponible".equals(vehiculo.getEstado())
                && !"Alquilado".equals(vehiculo.getEstado())
                && !"Mantenimiento".equals(vehiculo.getEstado())) {
            throw new IllegalArgumentException(
                    "El estado debe ser Disponible, Alquilado o Mantenimiento");
        }

        if (vehiculo.getOficina() == null
                || vehiculo.getOficina().getIdOficina() == null) {
            throw new IllegalArgumentException(
                    "Debes indicar la oficina del vehiculo");
        }
    }
}