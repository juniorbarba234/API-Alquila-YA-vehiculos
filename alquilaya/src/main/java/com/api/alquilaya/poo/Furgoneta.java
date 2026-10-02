package com.api.alquilaya.poo;

public class Furgoneta extends Vehiculo {

    private int capacidadCarga;

    public Furgoneta(String matricula,
                     double tarifaDiaria,
                     int capacidadCarga) {
        super(matricula, tarifaDiaria);

        if (capacidadCarga <= 0) {
            throw new IllegalArgumentException(
                    "La capacidad debe ser mayor que cero");
        }

        this.capacidadCarga = capacidadCarga;
    }

    @Override
    public double calcularPrecioAlquiler(int dias) {
        validarDias(dias);

        double precio = getTarifaDiaria() * dias;

        if (capacidadCarga > 1000) {
            precio = precio + (15 * dias);
        }

        return precio;
    }

    public int getCapacidadCarga() {
        return capacidadCarga;
    }

    public void setCapacidadCarga(int capacidadCarga) {
        this.capacidadCarga = capacidadCarga;
    }
}