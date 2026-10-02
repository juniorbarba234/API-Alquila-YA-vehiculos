package com.api.alquilaya.services;

import com.api.alquilaya.models.OficinaModel;
import com.api.alquilaya.repositories.OficinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OficinaService {

    private final OficinaRepository oficinaRepository;

    public OficinaService(OficinaRepository oficinaRepository) {
        this.oficinaRepository = oficinaRepository;
    }

    public List<OficinaModel> listar() {
        return oficinaRepository.findAll();
    }

    public OficinaModel buscarPorId(Long id) {
        return oficinaRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("La oficina no existe"));
    }

    public OficinaModel crear(OficinaModel oficina) {
        validar(oficina);
        oficina.setIdOficina(null);
        return oficinaRepository.save(oficina);
    }

    public OficinaModel actualizar(Long id, OficinaModel datos) {
        OficinaModel oficina = buscarPorId(id);
        validar(datos);

        oficina.setNombre(datos.getNombre());
        oficina.setDireccion(datos.getDireccion());
        oficina.setCiudad(datos.getCiudad());
        oficina.setRegion(datos.getRegion());

        return oficinaRepository.save(oficina);
    }

    public void eliminar(Long id) {
        OficinaModel oficina = buscarPorId(id);
        oficinaRepository.delete(oficina);
    }

    private void validar(OficinaModel oficina) {
        if (oficina.getNombre() == null
                || oficina.getNombre().isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la oficina es obligatorio");
        }

        if (oficina.getDireccion() == null
                || oficina.getDireccion().isBlank()) {
            throw new IllegalArgumentException(
                    "La direccion es obligatoria");
        }

        if (oficina.getCiudad() == null
                || oficina.getCiudad().isBlank()) {
            throw new IllegalArgumentException(
                    "La ciudad es obligatoria");
        }

        if (oficina.getRegion() == null
                || oficina.getRegion().isBlank()) {
            throw new IllegalArgumentException(
                    "La region es obligatoria");
        }
    }
}