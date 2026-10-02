package com.api.alquilaya.poo;

public class Coche extends Vehiculo {

    public Coche(String matricula, double tarifaDiaria) {
        super(matricula, tarifaDiaria);
    }

    @Override
    public double calcularPrecioAlquiler(int dias) {
        validarDias(dias);

        double precio = getTarifaDiaria() * dias;

        if (dias >= 7) {
            precio = precio * 0.90;
        }

        return precio;
    }
}