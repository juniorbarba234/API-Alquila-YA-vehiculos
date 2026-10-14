package com.api.alquilaya;

import com.api.alquilaya.models.*;
import com.api.alquilaya.services.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class InterfazIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired OficinaService oficinas;
    @Autowired ClienteService clientes;
    @Autowired VehiculoService vehiculos;
    @Autowired AlquilerService alquileres;

    @Test
    void registraAlquilerDesdeJsonDeLaInterfaz() throws Exception {
        VehiculoModel v = vehiculo();
        ClienteModel c = cliente();
        String json = "{\"cliente\":{\"idCliente\":" + c.getIdCliente()
                + "},\"vehiculo\":{\"idVehiculo\":" + v.getIdVehiculo()
                + "},\"fechaAlquiler\":\"2026-10-14\",\"fechaDevolucion\":\"2026-10-17\"}";
        var result = mvc.perform(post("/api/alquileres").contentType("application/json").content(json)).andReturn();
        assertEquals(200, result.getResponse().getStatus(), () -> String.valueOf(result.getResolvedException()));
    }

    @Test
    void sirveLaInterfazYLasListas() throws Exception {
        mvc.perform(get("/index.html")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Registrar alquiler")));
        mvc.perform(get("/app.js")).andExpect(status().isOk());
        mvc.perform(get("/estilos.css")).andExpect(status().isOk());
        for (String ruta : new String[]{"oficinas", "clientes", "vehiculos", "alquileres"}) {
            mvc.perform(get("/api/" + ruta)).andExpect(status().isOk());
        }
    }

    @Test
    void conservaDniAlCrearYEditar() {
        ClienteModel c = cliente();
        assertNotNull(c.getDni());
        String dni = c.getDni();
        c.setNombre("Cliente editado");
        assertEquals(dni, clientes.actualizar(c.getIdCliente(), c).getDni());
    }

    @Test
    void calculaTotalYDevuelveVehiculoSinReabrirAlquilerAnterior() {
        VehiculoModel v = vehiculo();
        ClienteModel c = cliente();
        AlquilerModel a = alquileres.crear(solicitud(v, c));
        assertEquals(0, new BigDecimal("1500").compareTo(a.getPrecioTotal()));
        assertEquals("Alquilado", vehiculos.buscarPorId(v.getIdVehiculo()).getEstado());
        assertThrows(IllegalArgumentException.class, () -> alquileres.crear(solicitud(v, c)));
        alquileres.completar(a.getIdAlquiler());
        assertEquals("Disponible", v.getEstado());
        alquileres.crear(solicitud(v, c));
        assertThrows(IllegalArgumentException.class, () -> alquileres.completar(a.getIdAlquiler()));
        assertEquals("Alquilado", v.getEstado());
    }

    @Test
    void rechazaFechasInvertidas() {
        AlquilerModel a = solicitud(vehiculo(), cliente());
        a.setFechaDevolucion(a.getFechaAlquiler().minusDays(1));
        assertThrows(IllegalArgumentException.class, () -> alquileres.crear(a));
    }

    private ClienteModel cliente() {
        String codigo = UUID.randomUUID().toString();
        return clientes.crear(new ClienteModel(codigo.substring(0, 9), "Prueba", "Interfaz",
                codigo + "@example.com", codigo, "5550000000"));
    }

    private VehiculoModel vehiculo() {
        OficinaModel o = new OficinaModel();
        o.setNombre("Prueba interfaz"); o.setDireccion("Calle de prueba");
        o.setCiudad("Ciudad"); o.setRegion("Centro");
        o = oficinas.crear(o);
        return vehiculos.crear(new VehiculoModel("Toyota", "Corolla", 2023,
                UUID.randomUUID().toString().substring(0, 12), new BigDecimal("500"), "Disponible", o));
    }

    private AlquilerModel solicitud(VehiculoModel v, ClienteModel c) {
        AlquilerModel a = new AlquilerModel();
        a.setVehiculo(v); a.setCliente(c);
        a.setFechaAlquiler(LocalDate.now()); a.setFechaDevolucion(LocalDate.now().plusDays(3));
        return a;
    }
}

