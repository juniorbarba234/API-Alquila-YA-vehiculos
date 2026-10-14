package com.api.alquilaya.services;

import com.api.alquilaya.models.ClienteModel;
import com.api.alquilaya.repositories.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<ClienteModel> listar() {
        return clienteRepository.findAll();
    }

    public ClienteModel buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("El cliente no existe"));
    }

    public ClienteModel crear(ClienteModel datos) {
        validar(datos);

        ClienteModel cliente = new ClienteModel();
        copiarDatos(cliente, datos);

        return clienteRepository.save(cliente);
    }

    public ClienteModel actualizar(Long id, ClienteModel datos) {
        ClienteModel cliente = buscarPorId(id);
        validar(datos);
        copiarDatos(cliente, datos);

        return clienteRepository.save(cliente);
    }

    public void eliminar(Long id) {
        ClienteModel cliente = buscarPorId(id);

        if (cliente.getAlquileres() != null
                && !cliente.getAlquileres().isEmpty()) {
            throw new IllegalArgumentException(
                    "No se puede eliminar un cliente que tiene alquileres");
        }

        clienteRepository.delete(cliente);
    }

    private void copiarDatos(ClienteModel cliente, ClienteModel datos) {
        if (datos.getDni() != null) {
            cliente.setDni(datos.getDni());
        }
        cliente.setNombre(datos.getNombre());
        cliente.setApellido(datos.getApellido());
        cliente.setEmail(datos.getEmail());
        cliente.setLicenciaConducir(datos.getLicenciaConducir());
        cliente.setTelefono(datos.getTelefono());
    }

    private void validar(ClienteModel cliente) {
        if (cliente.getNombre() == null
                || cliente.getNombre().isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre es obligatorio");
        }

        if (cliente.getApellido() == null
                || cliente.getApellido().isBlank()) {
            throw new IllegalArgumentException(
                    "El apellido es obligatorio");
        }

        if (cliente.getEmail() == null
                || cliente.getEmail().isBlank()
                || !cliente.getEmail().contains("@")) {
            throw new IllegalArgumentException(
                    "El email debe contener @");
        }

        if (cliente.getLicenciaConducir() == null
                || cliente.getLicenciaConducir().isBlank()) {
            throw new IllegalArgumentException(
                    "La licencia de conducir es obligatoria");
        }
    }
}
