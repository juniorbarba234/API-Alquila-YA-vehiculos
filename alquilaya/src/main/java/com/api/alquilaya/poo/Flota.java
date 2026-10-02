package com.api.alquilaya.poo;

import java.util.ArrayList;
import java.util.List;

public class Flota {

    private List<Vehiculo> vehiculos = new ArrayList<>();

    public void agregar(Vehiculo vehiculo) {
        vehiculos.add(vehiculo);
    }

    public void mostrarPresupuestos(int dias) {
        for (Vehiculo vehiculo : vehiculos) {
            double precio = vehiculo.calcularPrecioAlquiler(dias);

            System.out.println(
                    vehiculo.getTipo()
                            + " - "
                            + vehiculo.getMatricula()
                            + " -> "
                            + precio
                            + " euros");
        }
    }
}