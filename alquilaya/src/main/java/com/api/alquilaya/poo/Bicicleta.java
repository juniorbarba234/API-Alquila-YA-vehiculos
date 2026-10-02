package com.api.alquilaya.poo;

public class Bicicleta extends Vehiculo {

    private boolean electrica;

    public Bicicleta(String matricula,
                     double tarifaDiaria,
                     boolean electrica) {
        super(matricula, tarifaDiaria);
        this.electrica = electrica;
    }

    @Override
    public double calcularPrecioAlquiler(int dias) {
        validarDias(dias);

        double precio = getTarifaDiaria() * dias;

        if (electrica) {
            precio = precio + (2 * dias);
        }

        return precio;
    }

    public boolean isElectrica() {
        return electrica;
    }

    public void setElectrica(boolean electrica) {
        this.electrica = electrica;
    }
}