package com.api.alquilaya.services;

import com.api.alquilaya.models.MantenimientoModel;
import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.repositories.MantenimientoRepository;
import com.api.alquilaya.repositories.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MantenimientoService {

    private final MantenimientoRepository mantenimientoRepository;
    private final VehiculoRepository vehiculoRepository;

    public MantenimientoService(
            MantenimientoRepository mantenimientoRepository,
            VehiculoRepository vehiculoRepository) {
        this.mantenimientoRepository = mantenimientoRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    public List<MantenimientoModel> listar() {
        return mantenimientoRepository.findAll();
    }

    public MantenimientoModel buscarPorId(Long id) {
        return mantenimientoRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El mantenimiento no existe"));
    }

    @Transactional
    public MantenimientoModel crear(MantenimientoModel datos) {
        validar(datos);

        MantenimientoModel mantenimiento = new MantenimientoModel();
        copiarDatos(mantenimiento, datos);

        return mantenimientoRepository.save(mantenimiento);
    }

    @Transactional
    public MantenimientoModel actualizar(
            Long id, MantenimientoModel datos) {
        MantenimientoModel mantenimiento = buscarPorId(id);
        validar(datos);
        copiarDatos(mantenimiento, datos);

        return mantenimientoRepository.save(mantenimiento);
    }

    @Transactional
    public void eliminar(Long id) {
        MantenimientoModel mantenimiento = buscarPorId(id);
        mantenimientoRepository.delete(mantenimiento);
    }

    private void copiarDatos(
            MantenimientoModel mantenimiento,
            MantenimientoModel datos) {
        Long idVehiculo = datos.getVehiculo().getIdVehiculo();

        VehiculoModel vehiculo = vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El vehiculo no existe"));

        mantenimiento.setVehiculo(vehiculo);
        mantenimiento.setFechaMantenimiento(datos.getFechaMantenimiento());
        mantenimiento.setDescripcion(datos.getDescripcion());
        mantenimiento.setCosto(datos.getCosto());
        mantenimiento.setKilometraje(datos.getKilometraje());
    }

    private void validar(MantenimientoModel mantenimiento) {
        if (mantenimiento.getVehiculo() == null
                || mantenimiento.getVehiculo().getIdVehiculo() == null) {
            throw new IllegalArgumentException(
                    "Debes indicar el vehiculo");
        }

        if (mantenimiento.getFechaMantenimiento() == null) {
            throw new IllegalArgumentException(
                    "La fecha del mantenimiento es obligatoria");
        }

        if (mantenimiento.getDescripcion() == null
                || mantenimiento.getDescripcion().isBlank()) {
            throw new IllegalArgumentException(
                    "La descripcion es obligatoria");
        }

        if (mantenimiento.getCosto() == null
                || mantenimiento.getCosto().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "El costo no puede ser negativo");
        }

        if (mantenimiento.getKilometraje() == null
                || mantenimiento.getKilometraje() < 0) {
            throw new IllegalArgumentException(
                    "El kilometraje no puede ser negativo");
        }
    }
}