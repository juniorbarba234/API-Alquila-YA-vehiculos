package com.api.alquilaya.poo;

public class Moto extends Vehiculo {

    private int cilindrada;

    public Moto(String matricula,
                double tarifaDiaria,
                int cilindrada) {
        super(matricula, tarifaDiaria);

        if (cilindrada <= 0) {
            throw new IllegalArgumentException(
                    "La cilindrada debe ser mayor que cero");
        }

        this.cilindrada = cilindrada;
    }

    @Override
    public double calcularPrecioAlquiler(int dias) {
        validarDias(dias);

        double precio = (getTarifaDiaria() + 3) * dias;

        if (cilindrada > 500) {
            precio = precio + (5 * dias);
        }

        return precio;
    }

    public int getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(int cilindrada) {
        this.cilindrada = cilindrada;
    }
}