# API Alquila YA (vehiculos)

Aplicación para administrar oficinas, clientes, vehículos y alquileres. Incluye una interfaz sencilla con formularios y tablas.

## Funciones de la interfaz

- Registrar, consultar, editar y eliminar oficinas, clientes y vehículos.
- Elegir un cliente y un vehículo disponible para registrar un alquiler.
- Mostrar el total según los días y la tarifa diaria.
- Completar un alquiler para volver a disponer del vehículo.
- Mostrar mensajes cuando los datos no son válidos.

## Funcionamiento

Primero se registra una oficina, un cliente y un vehículo. Después se crea el alquiler seleccionando el cliente, el vehículo y las fechas. El servidor calcula el importe y cambia el vehículo a Alquilado. Al completar el alquiler, vuelve a estar Disponible.

El código del proyecto está en la carpeta `alquilaya`. La interfaz está en `alquilaya/src/main/resources/static` y las instrucciones de uso están en [INTERFAZ.md](alquilaya/INTERFAZ.md).
