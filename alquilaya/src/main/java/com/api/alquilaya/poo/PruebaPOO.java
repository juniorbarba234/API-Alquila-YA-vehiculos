package com.api.alquilaya.poo;

public class PruebaPOO {

    public static void main(String[] args) {

        Flota flota = new Flota();

        flota.agregar(new Coche("CAR001", 200));
        flota.agregar(new Moto("MOT001", 180, 600));
        flota.agregar(new Furgoneta("FUR001", 300, 1500));
        flota.agregar(new Bicicleta("BIC001", 50, true));
        flota.agregar(new CocheElectrico("ELE001", 300));

        flota.mostrarPresupuestos(7);

        try {
            new Bicicleta("", 50, false);
        } catch (IllegalArgumentException error) {
            System.out.println(
                    "Error controlado: " + error.getMessage());
        }
    }
}