# Interfaz de AlquilaYa

Abre una terminal en esta carpeta y ejecuta:

```powershell
.\EJECUTAR.cmd
```

Cuando termine de iniciar, abre http://localhost:8080 en el navegador. MySQL debe estar encendido y la conexión configurada.

En otra computadora, configura la contraseña mediante la variable de entorno `DB_PASSWORD` o un archivo `application-local.properties` en la carpeta del proyecto. Ese archivo es privado y está excluido de Git.

## Cómo usarla

1. En Oficinas, registra una oficina.
2. En Clientes, registra un cliente.
3. En Vehículos, registra un vehículo y selecciona su oficina.
4. En Alquileres, elige el cliente, un vehículo disponible y las fechas. Pulsa Registrar alquiler.
5. La tabla muestra el total que calcula el servidor. Completar registra la devolución y vuelve a dejar disponible el vehículo.

Los clientes, oficinas y vehículos se pueden consultar, editar y eliminar. Si un registro está relacionado con otros datos, la API puede impedir su eliminación. Los vehículos alquilados se modifican después de completar su alquiler.

## Cómo explicarla

La página tiene tres archivos en `src/main/resources/static`:

- `index.html`: contiene los formularios y tablas.
- `estilos.css`: define colores, tamaños y adaptación a pantallas pequeñas.
- `app.js`: envía solicitudes a la API y muestra las respuestas.

Los formularios envían POST para crear, PUT para editar y DELETE para eliminar. GET consulta las listas. Los datos se guardan en MySQL mediante los controladores y servicios de Java. El cálculo definitivo del precio lo hace el servicio de alquileres.
