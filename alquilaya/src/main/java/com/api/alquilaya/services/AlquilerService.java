package com.api.alquilaya.services;

import com.api.alquilaya.models.AlquilerModel;
import com.api.alquilaya.models.ClienteModel;
import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.repositories.AlquilerRepository;
import com.api.alquilaya.repositories.ClienteRepository;
import com.api.alquilaya.repositories.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AlquilerService {

    private final AlquilerRepository alquilerRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;

    public AlquilerService(AlquilerRepository alquilerRepository,
                           VehiculoRepository vehiculoRepository,
                           ClienteRepository clienteRepository) {
        this.alquilerRepository = alquilerRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<AlquilerModel> listar() {
        return alquilerRepository.findAll();
    }

    public AlquilerModel buscarPorId(Long id) {
        return alquilerRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("El alquiler no existe"));
    }

    @Transactional
    public AlquilerModel crear(AlquilerModel datos) {
        if (datos.getVehiculo() == null
                || datos.getVehiculo().getIdVehiculo() == null) {
            throw new IllegalArgumentException(
                    "Debes indicar el vehiculo");
        }

        if (datos.getCliente() == null
                || datos.getCliente().getIdCliente() == null) {
            throw new IllegalArgumentException(
                    "Debes indicar el cliente");
        }

        if (datos.getFechaAlquiler() == null
                || datos.getFechaDevolucion() == null
                || !datos.getFechaDevolucion()
                .isAfter(datos.getFechaAlquiler())) {
            throw new IllegalArgumentException(
                    "Las fechas no son validas");
        }

        VehiculoModel vehiculo = vehiculoRepository
                .findById(datos.getVehiculo().getIdVehiculo())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El vehiculo no existe"));

        ClienteModel cliente = clienteRepository
                .findById(datos.getCliente().getIdCliente())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El cliente no existe"));

        if (!"Disponible".equals(vehiculo.getEstado())) {
            throw new IllegalArgumentException(
                    "El vehiculo no esta disponible");
        }

        long dias = ChronoUnit.DAYS.between(
                datos.getFechaAlquiler(),
                datos.getFechaDevolucion());

        BigDecimal precio = vehiculo.getTarifaDiaria()
                .multiply(BigDecimal.valueOf(dias));

        AlquilerModel alquiler = new AlquilerModel();
        alquiler.setVehiculo(vehiculo);
        alquiler.setCliente(cliente);
        alquiler.setFechaAlquiler(datos.getFechaAlquiler());
        alquiler.setFechaDevolucion(datos.getFechaDevolucion());
        alquiler.setPrecioTotal(precio);
        alquiler.setEstado("Activo");

        vehiculo.setEstado("Alquilado");
        vehiculoRepository.save(vehiculo);

        return alquilerRepository.save(alquiler);
    }

    @Transactional
    public AlquilerModel completar(Long id) {
        AlquilerModel alquiler = buscarPorId(id);

        if (!"Activo".equals(alquiler.getEstado())) {
            throw new IllegalArgumentException("El alquiler ya no esta activo");
        }

        alquiler.setEstado("Completado");

        VehiculoModel vehiculo = alquiler.getVehiculo();
        vehiculo.setEstado("Disponible");
        vehiculoRepository.save(vehiculo);

        return alquilerRepository.save(alquiler);
    }

    @Transactional
    public void eliminar(Long id) {
        AlquilerModel alquiler = buscarPorId(id);
        alquilerRepository.delete(alquiler);
    }
}
