package com.api.alquilaya.poo;

public abstract class Vehiculo implements Alquilable {

    private String matricula;
    private double tarifaDiaria;
    private boolean disponible;

    public Vehiculo(String matricula, double tarifaDiaria) {
        if (matricula == null || matricula.isBlank()) {
            throw new IllegalArgumentException(
                    "La matricula es obligatoria");
        }

        if (tarifaDiaria <= 0) {
            throw new IllegalArgumentException(
                    "La tarifa debe ser mayor que cero");
        }

        this.matricula = matricula;
        this.tarifaDiaria = tarifaDiaria;
        this.disponible = true;
    }

    public abstract double calcularPrecioAlquiler(int dias);

    protected void validarDias(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException(
                    "Los dias deben ser mayores que cero");
        }
    }

    @Override
    public void alquilar() {
        if (!disponible) {
            throw new IllegalStateException(
                    "El vehiculo no esta disponible");
        }

        disponible = false;
    }

    @Override
    public void devolver() {
        disponible = true;
    }

    @Override
    public boolean estaDisponible() {
        return disponible;
    }

    public String getMatricula() {
        return matricula;
    }

    public double getTarifaDiaria() {
        return tarifaDiaria;
    }

    public String getTipo() {
        return getClass().getSimpleName();
    }
}