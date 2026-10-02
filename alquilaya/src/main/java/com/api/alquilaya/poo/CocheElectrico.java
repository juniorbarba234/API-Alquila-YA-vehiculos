package com.api.alquilaya.poo;

public class CocheElectrico extends Coche
        implements Electrico {

    private int nivelBateria;

    public CocheElectrico(String matricula,
                          double tarifaDiaria) {
        super(matricula, tarifaDiaria);
        nivelBateria = 0;
    }

    @Override
    public double calcularPrecioAlquiler(int dias) {
        double precioCoche = super.calcularPrecioAlquiler(dias);
        return precioCoche * 0.85;
    }

    @Override
    public void cargarBateria() {
        nivelBateria = 100;
    }

    @Override
    public int getNivelBateria() {
        return nivelBateria;
    }
}